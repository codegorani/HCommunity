let isUsernameValid = false;
let isNicknameValid = false;
let isEmailValid = false;
let isPasswordValid = false;
let isConfirmPasswordValid = false;
let isBirthValid = false;
let isLastNameValid = false;
let isFirstNameValid = false;
let isAddrValid = false;
let isPhoneNumValid = false;
let isUsernameAuthenticated = false;
let isNicknameAuthenticated = false;

const signup = {
    init: function() {
        const _this = this;

        $('#phoneNum').keyup(function (event) {
            event = event || window.event;
            let _val = this.value.trim();
            this.value = _this.numInput(_val);
            _this.phoneNumValidate();
        });

        $('#telNum').keyup(function (event) {
            event = event || window.event;
            let _val = this.value.trim();
            this.value = _this.numInput(_val);
        });

        $('#btn-search-addr').on('click', function() {
            _this.findAddr();
        });

        $('#email').on('keyup', function () {
            _this.emailValidate();
        });

        $('#lastName').on('blur', function () {
            _this.lastNameValidate();
        });

        $('#firstName').on('blur', function () {
            _this.firstNameValidate();
        });

        $('#password').on('keyup', function () {
            _this.passwordValidate();
        });

        $('#password-confirm').on('keyup', function () {
            _this.passwordEqual();
        });

        $('#username').on('keyup', function() {
            _this.usernameValidate();
        });

        $('#nickname').on('keyup', function() {
            _this.nicknameValidate();
        });

        $('#birth-year').on('change', function () {
            _this.birthValidate();
        });

        $('#birth-month').on('change', function () {
            _this.birthValidate();
        });

        $('#birth-day').on('change', function () {
            _this.birthValidate();
        });

        $('#btn-username-auth').on('click', function() {
            _this.usernameAuthenticate();
        });

        $('#btn-nickname-auth').on('click', function() {
            _this.nicknameAuthenticate();
        });

        $('#btn-user-save').on('click', function(e) {
            if((isUsernameValid && isNicknameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid
            && isBirthValid && isFirstNameValid && isLastNameValid && isAddrValid) !== true) {
                if(!isUsernameValid) {
                    _this.usernameValidate();
                    $('#username').focus();
                } else if(!isPasswordValid) {
                    _this.passwordValidate();
                    $('#password').focus();
                } else if(!isConfirmPasswordValid) {
                    _this.passwordEqual();
                    $('#password-confirm').focus();
                } else if(!isEmailValid){
                    _this.emailValidate();
                    $('#email').focus();
                } else if(!isBirthValid) {
                    _this.birthValidate();
                    $('#birth-year').focus();
                } else if(!isFirstNameValid) {
                    _this.firstNameValidate();
                    $('#firstName').focus();
                } else if(!isLastNameValid) {
                    _this.lastNameValidate();
                    $('#lastName').focus();
                } else if(!isAddrValid) {
                    $('#address1').focus();
                } else if(!isPhoneNumValid) {
                    _this.phoneNumValidate();
                    $('#phoneNum').focus();
                } else if(!isNicknameValid) {
                    _this.nicknameValidate()
                    $('#nickname').focus();
                }
                alert('작성 항목을 다시 검토하세요.');
                return;
            }
            if(!isUsernameAuthenticated) {
                alert('아이디 중복체크를 먼저 진행하세요.')
                $('#btn-username-auth').focus();
                return;
            }
            if(!isNicknameAuthenticated) {
                alert('닉네임 중복체크를 먼저 진행하세요.')
                $('#btn-nickname-auth').focus();
                return;
            }
            if(confirm('가입하시겠습니까?')) {
                _this.saveUser();
            }
        });
    },

    phoneNumValidate: function() {
        const data = $('#phoneNum').val();

        if (/^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}/.test(data)) {
            $('#phoneNum-warning').css('display', 'none');
            $('#phoneNum-good').css('display', 'block');
            isPhoneNumValid = true;
        } else {
            $('#phoneNum-warning').css('display', 'block');
            $('#phoneNum-good').css('display', 'none');
            isPhoneNumValid = false;
        }
   },


    nicknameAuthenticate: function () {
        const data = $('#nickname').val();

        $.ajax({
            url: '/nickname/auth',
            method: 'POST',
            data: data,
            dataType: 'text',
            contentType: 'text/plain; charset=utf-8',
            success: function(data) {
                if (data === 'valid') {
                    alert('사용 가능한 닉네임 입니다.');
                    $('#btn-nickname-auth').attr('disabled', true);
                    $('#nickname').attr('readonly', true);
                    $('#nickname-auth').css('display', 'block');
                    isNicknameAuthenticated = true;
                } else {
                    alert('이미 사용중인 닉네임 입니다.');
                    $('#nickname').focus();
                }
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    },

    usernameAuthenticate: function () {
        const data = $('#username').val();

        $.ajax({
            url: '/username/auth',
            method: 'POST',
            data: data,
            dataType: 'text',
            contentType: 'text/plain; charset=utf-8',
            success: function(data) {
                if (data === 'valid') {
                    alert('사용 가능한 아이디 입니다.');
                    $('#btn-username-auth').attr('disabled', true);
                    $('#username').attr('readonly', true);
                    $('#username-auth').css('display', 'block');
                    isUsernameAuthenticated = true;
                } else {
                    alert('이미 사용중인 아이디 입니다.');
                    $('#username').focus();
                }
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    },

    birthValidate: function () {
        const birthYear = $('#birth-year option:selected').val();
        const birthMonth = $('#birth-month option:selected').val();
        const birthDay = $('#birth-day option:selected').val();
        if (birthYear === 'none' || birthMonth === 'none' || birthDay === 'none') {
            $('#birth-warn').css('display', 'block');
            $('#birth-good').css('display', 'none');
            isBirthValid = false;
        } else {
            $('#birth-warn').css('display', 'none');
            $('#birth-good').css('display', 'block');
            let strBirthDay = birthDay
            let strBirthMonth = birthMonth
            if (parseInt(birthDay) < 10) {
                strBirthDay = '0' + birthDay;
            }
            if (parseInt(birthMonth) < 10) {
                strBirthMonth = '0' + birthMonth;
            }

            $('#birth').val(birthYear + '-' + strBirthMonth + '-' + strBirthDay);
            if($('#birth').val().length === 10) {
                $('#birth-warn').css('display', 'none');
                $('#birth-good').css('display', 'block');
                isBirthValid = true;
            } else {
                $('#birth-warn').css('display', 'block');
                $('#birth-good').css('display', 'none');
                isBirthValid = false;
            }
        }
    },

    nicknameValidate: function () {
        const nickname = $('#nickname').val();
        const special = /[`~!@#$%^&*|\\\'\";:\/?]/gi;
        const blank = /[\s]/g;

        if((special.test(nickname) || blank.test(nickname)) === false && nickname.length >= 2) {
            $('#nickname-good').css('display', 'block');
            $('#nickname-warning').css('display', 'none');
            isNicknameValid = true;
        } else {
            $('#nickname-good').css('display', 'none');
            $('#nickname-warning').css('display', 'block');
            isNicknameValid = false;
        }

    },

    usernameValidate: function () {
        const username = $('#username').val();
        const usernamePattern = /^[a-zA-z0-9]{4,12}$/;

        if(usernamePattern.test(username)) {
            $('#username-good').css('display', 'block');
            $('#username-warning').css('display', 'none');
            isUsernameValid = true;
        } else {
            $('#username-good').css('display', 'none');
            $('#username-warning').css('display', 'block');
            isUsernameValid = false;
        }
    },

    passwordEqual: function () {
        const myPassword = $('#password').val();
        const confirmPassword = $('#password-confirm').val();
        if (myPassword !== confirmPassword) {
            $('#password-warn').css('display', 'block');
            $('#password-confirm-good').css('display', 'none');
            isConfirmPasswordValid = false;
        } else {
            $('#password-warn').css('display', 'none');
            $('#password-confirm-good').css('display', 'block');
            //$('#password').attr('readonly', true);
            isConfirmPasswordValid = true;
        }
    },

    passwordValidate: function () {
        const password = $('#password').val();

        const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;

        let isGoodPattern;
        let isGoodLength;

        if (passwordPattern.test(password)) {
            $('#password-warning').css('display', 'none');
            isGoodPattern = true;
        } else {
            $('#password-warning').css('display', 'block');
            isGoodPattern = false;
        }
        if (password.length < 9 || password.length > 13) {
            $('#password-length-warning').css('display', 'block');
            isGoodLength = false;
        } else {
            $('#password-length-warning').css('display', 'none');
            isGoodLength = true;
        }

        if (isGoodLength && isGoodPattern) {
            $('#password-good').css('display', 'block');
            isPasswordValid = true;
        } else {
            $('#password-good').css('display', 'none');
            isPasswordValid = false;
        }
    },

    firstNameValidate: function () {
        const nameValue = $('#firstName').val();
        if (nameValue.length <= 1) {
            $('#firstName-warning').css('display', 'block');
            $('#firstName-good').css('display', 'none');
            isFirstNameValid = false;
        } else {
            $('#firstName-warning').css('display', 'none');
            $('#firstName-good').css('display', 'block');
            isFirstNameValid = true;
        }
    },

    lastNameValidate: function () {
        const nameValue = $('#lastName').val();
        if (nameValue.length === 0) {
            $('#lastName-warning').css('display', 'block');
            $('#lastName-good').css('display', 'none');
            isLastNameValid = false;
        } else {
            $('#lastName-warning').css('display', 'none');
            $('#lastName-good').css('display', 'block');
            isLastNameValid = true;
        }
    },

    emailValidate: function () {
        const email = $('#email').val();
        const emailRegex = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
        if (!emailRegex.test(email)) {
            $('#email-warning').css('display', 'block');
            $('#email-good').css('display', 'none');
            isEmailValid = false;
        } else {
            $('#email-warning').css('display', 'none');
            $('#email-good').css('display', 'block');
            isEmailValid = true;
        }
    },

    numInput: function(str) {
        str = str.replace(/[^0-9]/g, '');
        let tmp = '';

        if (str.substring(0, 2) === '02') {
            // 서울 전화번호일 경우 10자리까지만 나타나고 그 이상의 자리수는 자동삭제
            if (str.length < 3) {
                return str;
            } else if (str.length < 6) {
                tmp += str.substr(0, 2);
                tmp += '-';
                tmp += str.substr(2);
                return tmp;
            } else if (str.length < 10) {
                tmp += str.substr(0, 2);
                tmp += '-';
                tmp += str.substr(2, 3);
                tmp += '-';
                tmp += str.substr(5);
                return tmp;
            } else {
                tmp += str.substr(0, 2);
                tmp += '-';
                tmp += str.substr(2, 4);
                tmp += '-';
                tmp += str.substr(6, 4);
                return tmp;
            }
        } else {
            // 핸드폰 및 다른 지역 전화번호 일 경우
            if (str.length < 4) {
                return str;
            } else if (str.length < 7) {
                tmp += str.substr(0, 3);
                tmp += '-';
                tmp += str.substr(3);
                return tmp;
            } else if (str.length < 11) {
                tmp += str.substr(0, 3);
                tmp += '-';
                tmp += str.substr(3, 3);
                tmp += '-';
                tmp += str.substr(6);
                return tmp;
            } else {
                tmp += str.substr(0, 3);
                tmp += '-';
                tmp += str.substr(3, 4);
                tmp += '-';
                tmp += str.substr(7);
                return tmp;
            }
        }
    },

    saveUser: function() {
        const data = {
            'username': $('#username').val(),
            'password': $('#password').val(),
            'nickname': $('#nickname').val(),
            'lastName': $('#lastName').val(),
            'firstName': $('#firstName').val(),
            'birth': $('#birth').val(),
            'email': $('#email').val(),
            'phoneNum': $('#phoneNum').val(),
            'telNum': $('#telNum').val(),
            'address1': $('#address1').val(),
            'address2': $('#address2').val()
        }

        $.ajax({
            url: '/signup',
            method: 'post',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(data)
        }).done(function() {
            window.location.href = "/welcome"
        }).fail(function(error) {
            alert(JSON.stringify(error));
            location.href = '/error/' + error.status;
        });
    },

    findAddr: function() {
        new daum.Postcode({
            oncomplete: function(data) {
                const roadAddr = data.roadAddress;
                if(roadAddr.length > 0) {
                    isAddrValid = true;
                }
                $('#address1').val(roadAddr);
            }
        }).open();
    }
}

signup.init();
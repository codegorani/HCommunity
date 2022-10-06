const user = {
    init: function() {
        const _this = this;

        $('#btn-ajax').on('click', function() {
            _this.ajaxTest();
        });

        $('#btn-forgot-password').on('click', function() {
            _this.forgotPassword();
        });

        $('#new-password').on('keyup', function() {
            _this.passwordValidate();
            if($('#confirm-new-password').val() !== '') {
                _this.newPasswordEqual();
            }
        });

        $('#confirm-new-password').on('keyup', function() {
            _this.newPasswordEqual();
        });

        $('#btn-forgot-reset').on('click', function() {
            _this.passwordReset();
        });

        $('#btn-inactive-clear').on('click', function() {
            _this.inactiveClear();
        });
    },

    ajaxTest: function() {
        $.ajax({
            url: '/test1',
            method: 'post',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: ''
        }).done(function(param) {
            if(param.isSuccess === 'false') {
                alert('이미 구매함');
            }else {
                $.ajax({
                    url: '/test2',
                    method: 'post',
                    contentType: 'application/json; charset=utf-8',
                    dataType: 'json',
                    data: ''
                }).done(function(param) {
                    if(param.isSuccess === 'false') {
                        alert('오류');
                    } else {
                        alert('구매 성공');
                    }
                }).fail(function(error) {
                    alert(JSON.stringify(error))
                    location.href = '/error/' + error.status;
                })
            }
        }).fail(function(error) {
            alert(JSON.stringify(error))
            location.href = '/error/' + error.status;
        })
    },

    forgotPassword: function() {
        const data = {
            'username': $('#username').val(),
            'email': $('#email').val()
        };

        $.ajax({
            url: '/forgotPassword',
            method: 'POST',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(data)
        }).done(function(data) {
            if(data.result === 'FOUNDED') {
                alert('계정을 찾았습니다. 패스워드 변경 페이지로 이동합니다.');
                location.href = '/forgotPassword/reset/' + data.username;
            } else if(data.result === 'NOT_FOUNDED') {
                alert('계정을 찾을 수 없습니다.');
                location.reload();
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        })
    },

    newPasswordEqual: function() {
        const myPassword = $('#new-password').val();
        const confirmPassword = $('#confirm-new-password').val();
        if (myPassword !== confirmPassword) {
            $('#password-warn').css('display', 'block');
            $('#password-confirm-good').css('display', 'none');
            $('#password-confirm').css('border', 'solid 2px red');
            isConfirmPasswordValid = false;
        } else {
            $('#password-warn').css('display', 'none');
            $('#password-confirm-good').css('display', 'block');
            $('#password-confirm').css('border', 'solid 2px limegreen');
            //$('#password').attr('readonly', true);
            isConfirmPasswordValid = true;
        }
    },

    passwordValidate: function() {

        const password = $('#new-password').val();

        const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@!%*#?&]{8,}$/;

        let isGoodPattern = false;

        if (!passwordPattern.test(password) || (password.length < 9 || password.length > 13)) {
            $('#password-warning').css('display', 'block');
            isGoodPattern = false;
        } else {
            $('#password-warning').css('display', 'none');
            isGoodPattern = true;
        }

        if (isGoodPattern) {
            $('#password-good').css('display', 'block');
            $('#password').css('border', 'solid 2px limegreen');
            isPasswordValid = true;
        } else {
            $('#password-good').css('display', 'none');
            $('#password').css('border', 'solid 2px red');
            isPasswordValid = false;
        }
    },

    passwordReset: function() {
        const data = {
            username: window.location.href.substring(window.location.href.lastIndexOf('/') + 1),
            newPassword: $('#new-password').val(),
        }

        $.ajax({
            url: '/forgotPassword/reset',
            method: 'POST',
            contentType: 'application/json; charset=UTF-8',
            dataType: 'text',
            data: JSON.stringify(data)
        }).done(function(data) {
            if(data === 'SUCCESS') {
                alert('패스워드 변경이 완료되었습니다.');
                location.href = '/login';
            } else if(data === 'NEW_PASSWORD_IS_CURRENT') {
                alert('현재 사용중인 패스워드입니다.');
                location.reload();
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        })
    },

    inactiveClear: function() {
        const data = {
            'username': $('#username').val(),
            'password': $('#password').val(),
            'email': $('#email').val()
        };

        $.ajax({
            method: 'POST',
            url: '/inactive/clear',
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            data: JSON.stringify(data)
        }).done(function(data) {
           if(data === 'USERNAME_ERROR') {
               alert('존재하지 않는 아이디입니다.');
               location.reload();
           } else if(data === 'SUCCESS') {
               alert('휴면계정 해제가 완료되었습니다. 다시 로그인 해주세요');
               location.href = '/login';
           } else if(data === 'EMAIL_ERROR') {
               alert('이메일이 올바르지 않습니다.');
               location.reload();
           } else if(data === 'PASSWORD_ERROR') {
               alert('패스워드가 올바르지 않습니다.');
               location.reload();
           }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    }
}

user.init();
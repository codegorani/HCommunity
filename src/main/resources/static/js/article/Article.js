let isPasswordValid = false;
let isConfirmPasswordValid;


const article = {
    init: function () {
        const _this = this;

        $('#btn-article-save').on('click', function() {
            _this.articleSave();
        });

        $('#btn-like').on('click', function() {
            const idx = $(this).data('idx');
            _this.articleLike(idx);
        });

        $('#btn-dislike').on('click', function() {
            const idx = $(this).data('idx');
            _this.articleDislike(idx);
        });

        $('#btn-reply-save').on('click', function() {
            _this.replySave();
        });

        $('.btn-reply-delete').on('click', function() {
            if(confirm('삭제하시겠습니까?')) {
                const idx = $(this).data('idx');
                _this.replyRemove(idx);
            }
        });

        $('.btn-article-delete').on('click', function() {
            if(confirm('삭제하시겠습니까?')) {
                const idx = $(this).data('idx');
                const page = $(this).data('page');
                _this.articleDelete(idx, page);
            }
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

        $('#btn-password-reset').on('click', function() {
            _this.passwordReset();
        })
    },
    articleSave: function () {
        const data = {
            title: $('#title').val(),
            contents: CKEDITOR.instances['contents'].getData(),
            galleryName: $('#galleryName').val()
        };

        $.ajax({
            url: '/api/v1/article',
            method: 'POST',
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            data: JSON.stringify(data)
        }).done(function (data) {
            $(window).unbind('beforeunload');
            alert('완료되었습니다.');
            location.href = '/article/view/' + data;
        }).fail(function (error) {
            alert(JSON.stringify(error))
            location.href = '/error/' + error.status;
        })
    },
    articleLike: function(idx) {
        const data = {
            idx: idx,
            user: $('#user').val()
        };

        if(data.user === '0') {
            alert('로그인 후 이용 가능합니다.')
            location.href = '/login'
        } else {
            $.ajax({
                url: '/api/v1/article/like',
                method: 'POST',
                contentType: 'application/json; charset=utf-8',
                dataType: 'text',
                data: JSON.stringify(data)
            }).done(function () {
                alert('완료.');
                location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error))
                location.href = '/error/' + error.status;
            });
        }
    },
    articleDislike: function(idx) {
        const data = {
            idx: idx,
            user: $('#user').val()
        };

        if(data.user === '0') {
            alert('로그인 후 이용 가능합니다.')
            location.href = '/login'
        } else {
            $.ajax({
                url: '/api/v1/article/dislike',
                method: 'POST',
                contentType: 'application/json; charset=utf-8',
                dataType: 'text',
                data: JSON.stringify(data)
            }).done(function () {
                alert('완료.');
                location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error))
                location.href = '/error/' + error.status;
            })
        }
    },
    replySave: function() {
        const data = {
            idx: $('#article-idx').val(),
            replyContent: $('#reply-content').val()
        };

        $.ajax({
            url: '/api/v1/reply',
            method: 'POST',
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            data: JSON.stringify(data)
        }).done(function () {
            location.reload();
        }).fail(function (error) {
            alert(JSON.stringify(error))
            if(error.status === 401) {
                alert('로그인이 필요합니다.')
                location.href = '/login';
            }
        });
    },
    replyRemove: function(idx) {
        $.ajax({
            url: '/api/v1/reply/' + idx,
            method: 'DELETE',
            contentType: 'application/json; charset=utf-8',
        }).done(function () {
            location.reload();
        }).fail(function (error) {
            alert(JSON.stringify(error))
            location.href = '/error/' + error.status;
        })
    },
    articleDelete: function(idx, page) {

        $.ajax({
            url: '/api/v1/article/' + idx,
            method: 'DELETE',
            contentType: 'application/json; charset=utf-8',
        }).done(function () {
            if(page === 'all') {
                location.href = '/article';
            } else if(page === 'my') {
                location.reload();
            }
        }).fail(function (error) {
            alert(JSON.stringify(error))
            location.href = '/error/' + error.status;
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
            currentPassword: $('#current-password').val(),
            newPassword: $('#new-password').val()
        }

        $.ajax({
            url: '/api/v1/my/resetPassword',
            method: 'PUT',
            contentType: 'application/json; charset=UTF-8',
            dataType: 'text',
            data: JSON.stringify(data)
        }).done(function(data) {
            if(data === 'SUCCESS') {
                alert('패스워드 변경이 완료되었습니다. 재접속해주세요.');
                location.href = '/logout';
            } else if (data === 'CURRENT_PASSWORD_ERROR') {
                alert('현재 비밀번호가 맞지않습니다. 다시시도해주세요.');
                location.reload();
            } else if(data === 'NEW_PASSWORD_IS_CURRENT') {
                alert('현재 사용중인 패스워드입니다.');
                location.reload();
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        })
    }
}

article.init();
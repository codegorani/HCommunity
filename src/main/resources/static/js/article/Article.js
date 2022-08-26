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
        })
    },
    articleSave: function () {
        const data = {
            title: $('#title').val(),
            contents: CKEDITOR.instances['contents'].getData()
        };

        $.ajax({
            url: '/api/v1/article',
            method: 'POST',
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            data: JSON.stringify(data)
        }).done(function (data) {
            alert('완료되었습니다.');
            location.href = '/article/view/' + data;
        }).fail(function (error) {
            alert(JSON.stringify(error));
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
                url: '/api/v1/like',
                method: 'POST',
                contentType: 'application/json; charset=utf-8',
                dataType: 'text',
                data: JSON.stringify(data)
            }).done(function () {
                alert('완료.');
                location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
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
                url: '/api/v1/dislike',
                method: 'POST',
                contentType: 'application/json; charset=utf-8',
                dataType: 'text',
                data: JSON.stringify(data)
            }).done(function () {
                alert('완료.');
                location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
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
            alert(JSON.stringify(error));
        })
    },
    replyRemove: function(idx) {
        $.ajax({
            url: '/api/v1/reply/' + idx,
            method: 'DELETE',
            contentType: 'application/json; charset=utf-8',
        }).done(function () {
            location.reload();
        }).fail(function (error) {
            alert(JSON.stringify(error));
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
            alert(JSON.stringify(error));
        })
    }
}

article.init();
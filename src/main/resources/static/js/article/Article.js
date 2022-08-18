const article = {
    init: function () {
        const _this = this;

        $('#btn-article-save').on('click', function() {
            _this.articleSave();
        });
    },
    articleSave: function () {
        const data = {
            title: $('#title').val(),
            contents: CKEDITOR.instances['contents'].getData()
        };

        $.ajax({
            url: '/article/save',
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
    }
}

article.init();
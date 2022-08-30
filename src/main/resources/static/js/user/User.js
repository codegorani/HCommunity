const user = {
    init: function() {
        const _this = this;

        $('#btn-ajax').on('click', function() {
            _this.ajaxTest();
        })
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
    }
}

user.init();
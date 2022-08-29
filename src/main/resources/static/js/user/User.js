const user = {
    init: function() {
        const _this = this;

        $('#btnSave').on('click', function() {
            _this.saveUser();
        });

        $('#btn-ajax').on('click', function() {
            _this.ajaxTest();
        })
    },
    saveUser: function() {
        const data = {
            'username': 'hjhearts',
            'password': 'asd5689',
            'nickname': '하위',
            'lastName': '한',
            'firstName': '주성',
            'birth': '1995-10-29',
            'email': 'none@none.com',
            'phoneNum': '010-1234-5678',
            'telNum': '',
            'address1': '경기도 오산시 123-456',
            'address2': ''
        }

        $.ajax({
            url: '/signup',
            method: 'post',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(data)
        }).done(function() {
            console.log('done');
            window.location.href = "/"
        }).fail(function(error) {
            alert(JSON.stringify(error));
            location.href = '/error/' + error.status;
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
    }
}

user.init();
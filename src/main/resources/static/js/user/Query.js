const query = {
    init: function() {
        const _this = this;
        $('#btn-modify').on('click', function () {
            console.log('click')
            _this.modify();
        });
    },
    modify: function() {
        $('#title').attr('readonly', false);
        $('#content').attr('readonly', false);
        $('#btn-modify').attr('onclick', "location.href='/api/modify'");
        $('#btn-modify').attr('text', "수정완료");
    }
}
query.init();
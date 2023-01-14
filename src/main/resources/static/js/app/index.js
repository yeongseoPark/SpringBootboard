var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });

        $('#btn-update').on('click', function () {
            _this.update();
        });

        $('#btn-delete').on('click', function () {
            _this.delete();
        });

        $('#btn-comment-save').on('click', function () {
            _this.commentSave();
        });

        $('#btn-comment-delete').on('click', function () {
            _this.commentDelete();
        });

        $('#alert-save-btn').on('click', function() {
            _this.alertSave();
        });

       $('input[name=alert-start]').on('click', function(e) { //e는 input 정보를 가진 이벤트
           _this.alertStart(e);
       });
    },

    alertStart : function(e) {
        var id =  $(e.target).data("id");
        // target은 이벤트가 발생한 대상 객체
         $.ajax({
                            type: 'GET',
                            url: '/alerts/'+id,
                        }).done(function() {
                            alert('알림이 시작됩니다.');
                        }).fail(function (error) {
                            alert(JSON.stringify(error));
                        });
    },

    alertSave : function () {
        var data = {
            price : $('#price').val(),
            percentage : $('#percentage').val(),
            ticker : $('#contact').val(),
            alertType : $('#alertType').val()
        };

        $.ajax({
                    type: 'POST',
                    url: '/alerts/save',
                    dataType: 'json',
                    contentType:'application/json; charset=utf-8',
                    data: JSON.stringify(data)
                }).done(function() {
                    alert('알림이 등록되었습니다.');
                    window.location.href = '/alerts'; // 글 등록이 성공하면 메인페이지로 이동
                }).fail(function (error) {
                    alert(JSON.stringify(error));
                });
    },

    save : function () {
        var data = {
            title: $('#title').val(),
//            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 등록되었습니다.');
            window.location.href = '/'; // 글 등록이 성공하면 메인페이지로 이동
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update : function () {
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    delete : function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('글이 삭제되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    commentDelete : function (id) {
        var Postid = $('#btn-comment-delete').data('post-id');
        var commentId = $('#btn-comment-delete').data('comment-id');

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/'+ Postid + '/comments/' + commentId,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('댓글이 삭제되었습니다.');
            window.location.href = '/posts/' + Postid;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },


    commentSave : function () {
        var data = {
            postsId: $('#postsId').val(),
            comment: $('#comment').val()
        }
        // 공백 및 빈 문자열 체크
        if (!data.comment || data.comment.trim() === "") {
             alert("공백 또는 입력하지 않은 부분이 있습니다.");
             return false;
        } else {
            $.ajax({
                 type: 'POST',
                 url: '/api/v1/posts/' + data.postsId + '/comments',
                 dataType: 'JSON',
                 contentType: 'application/json; charset=utf-8',
                 data: JSON.stringify(data)
                 }).done(function () {
                        alert('댓글이 등록되었습니다.');
                        window.location.reload();
                 }).fail(function (error) {
                    alert(JSON.stringify(error));
                 });
             }
     }
};

main.init();
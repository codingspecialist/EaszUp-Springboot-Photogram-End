// 회원정보 수정
function update(userId, event) {

  event.preventDefault();
  let data = $("#profileUpdate").serialize();


  $.ajax({
    type: "put",
    url: "/user/" + userId,
    data: data,
    contentType: "application/x-www-form-urlencoded; charset=utf-8",
    dataType: "json",
  }).done((res) => {
    location.href = "/user/" + userId;
  });
}
/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기, 댓글삭제
 */

let page = 0;

let principalId = $("#principalId").val();
let principalUsername = $("#principalUsername").val();

function storyLoad() {
  // ajax로 Page<Image> 가져올 예정 (3개)
  $.ajax({
    type: "get",
    url: `/image?page=${page}`,
    dataType: "json",
  }).done((res) => {
    let images = res.data.content;
    images.forEach((image) => {
      let storyItem = getStoryItem(image);
      $("#storyList").append(storyItem);
    });
  });
}

storyLoad();

$(window).scroll(() => {

  let checkNum = $(window).scrollTop() - ($(document).height() - $(window).height());

  // 근사치 계산
  if (checkNum < 1 && checkNum > -1) {
    page++;
    storyLoad();
  }
});

function getStoryItem(image) {
  let result = `
<!--전체 리스트 아이템-->
<div class="story-list__item">
	<!--리스트 아이템 헤더영역-->
	<div class="sl__item__header">
		<div><img class="profile-image" src="/upload/${image.user.profileImageUrl}" alt=""  onerror="this.src='/images/person.jpeg'"/></div>
		<div>${image.user.username}</div>
	</div>
	<!--헤더영역 end-->

	<!--게시물이미지 영역-->
	<div class="sl__item__img">
		<img src="/upload/${image.postImageUrl}" alt="" />
	</div>

	<!--게시물 내용 + 댓글 영역-->
	<div class="sl__item__contents">
		<!-- 하트모양 버튼 박스 -->
		<div class="sl__item__contents__icon"> `;

  if (image.likeState) {
    result += `<button onclick="toggleLike(${image.id}, this)">
							<i class="fas fa-heart active" id="storyLikeIcon-${image.id}"></i>
						</button>`;
  } else {
    result += `<button onclick="toggleLike(${image.id}, this)">
							<i class="far fa-heart" id="storyLikeIcon-${image.id}"></i>
						</button>`;
  }

  result += `	
		</div>
		<!-- 하트모양 버튼 박스 end -->

		<!--좋아요-->
		<span class="like"><b id="storyLikeCount-${image.id}">${image.likeCount}</b>likes</span>
		<!--좋아요end-->

		<!--태그박스-->
		<div class="sl__item__contents__tags">
			<p> `;

  image.tags.forEach((tag) => {
    result += `#${tag.name} `;
  });

  result += `			
			</p>
		</div>
		<!--태그박스end-->

		<!--게시글내용-->
		<div class="sl__item__contents__content">
			<p>${image.caption}</p>
		</div>
		<!--게시글내용end-->
		
		<!-- 댓글 들어오는 박스 -->
		<div id="storyCommentList-${image.id}">
		`;

  image.comments.forEach((comment) => {
    result += `	<div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}"">
			    <p>
			      <b>${comment.user.username} :</b>
			      ${comment.content}
			    </p>
  				`;

    if (principalId == comment.user.id) {
      result += `
  				    <button onClick="deleteComment(${comment.id})"><i class="fas fa-times"></i></button>
  				`;
    }

    result += `
			  </div>`;
  });

  result += `
		</div>
		<!-- 댓글 들어오는 박스end -->

		<!--댓글입력박스-->
		<div class="sl__item__input">
			<input type="text" placeholder="댓글 달기..." id="storyCommentInput-${image.id}"/>
			<button type="button" onClick="addComment(${image.id}, '${principalUsername}')">게시</button>
		</div>
		<!--댓글달기박스end-->
	</div>
</div>
<!--전체 리스트 아이템end-->
`;
  return result;
}

function toggleLike(imageId) {
	let likeIcon = $("#storyLikeIcon-" + imageId);
	if (likeIcon.hasClass("far")) {
		$.ajax({
			type: "POST",
			url: `/image/${imageId}/likes`,
			dataType: "json"
		}).done(res => {
			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) + 1;
			$(`#storyLikeCount-${imageId}`).text(likeCount);

			likeIcon.addClass("fas");
			likeIcon.addClass("active");
			likeIcon.removeClass("far");
		});



	} else {
		$.ajax({
			type: "DELETE",
			url: `/image/${imageId}/likes`,
			dataType: "json"
		}).done(res => {
			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) - 1;
			$(`#storyLikeCount-${imageId}`).text(likeCount);

			likeIcon.removeClass("fas");
			likeIcon.removeClass("active");
			likeIcon.addClass("far");
		});

	}
}


function addComment(imageId, username) {

	let commentInput = $("#storyCommentInput-" + imageId);
	let commentList = $("#storyCommentList-" + imageId);
	
	let data = {
		content: commentInput.val()
	}
	

	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}

	// 통신 성공하면 아래 prepend 되야 되고 ID값 필요함
	$.ajax({
		type: "POST",
		url: `/image/${imageId}/comment`,
		data: data.content,
		contentType: "plain/text; charset=utf-8",
		dataType: "json"
	}).done(res => {
		let comment = res.data;
		let content = `
			  <div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}""> 
			    <p>
			      <b>${username} :</b>
			      ${comment.content}
			    </p>
			    <button onClick="deleteComment(${comment.id})"><i class="fas fa-times"></i></button>
			  </div>
			  `;
		commentList.prepend(content);
		commentInput.val("");
	});

}

function deleteComment(commentId) {
	$.ajax({
		type: "delete",
		url: "/comment/" + commentId,
		dataType: "json"
	}).done(res => {
		$("#storyCommentItem-" + commentId).remove();
	});
}







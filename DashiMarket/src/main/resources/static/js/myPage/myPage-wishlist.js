console.log('myPage-wishlist');

/* myPage-wishlist.js */
document.addEventListener("DOMContentLoaded", function() {
  const deleteButtons = document.querySelectorAll(".btn-delete");

  deleteButtons.forEach(btn => {
    btn.addEventListener("click", function() {
      const boardNo = this.dataset.id;

      if (!confirm("정말 삭제하시겠습니까?")) return;

      fetch(`/myPage/wishlist/delete/${boardNo}`, {
        method: "DELETE"
      })
      .then(response => {
        if (!response.ok) throw new Error("삭제 실패");
        return response.text();
      })
      .then(result => {
        if (result === "success") {
          alert("삭제되었습니다.");
          // 화면에서 해당 item 제거
          this.closest(".item").remove();
        } else {
          alert("삭제 실패");
        }
      })
      .catch(err => {
        console.error(err);
        alert("삭제 중 오류가 발생했습니다.");
      });
    });
  });
});

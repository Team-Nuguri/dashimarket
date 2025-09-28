console.log("communityDetail.js");

/* 정렬 */
const sort = document.querySelectorAll(".sort-area a");
let isSort = null;

sort.forEach(link => {
  link.addEventListener("click", (e) => {
    e.preventDefault();

    if(isSort) {
      isSort.classList.remove("bold-text");
    }

    link.classList.add("bold-text");
    isSort = link;
  })
})
console.log("admin.js loaded...")

// 사이드메뉴
const move = document.querySelectorAll(".list-group a");

// 로그아웃 버튼 이벤트 (모든 관리자 페이지 공통)
const logoutBtn = document.getElementById("logout");
if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
        if (confirm("로그아웃 하시겠습니까?")) {
            location.href = "/member/logout";
        }
    });
}

move.forEach(link => {
    link.addEventListener("click", (e) => {
        move.forEach(i => i.classList.remove("selected"));
        link.classList.add("selected");
    });
});

// ========================================
// ✅ 통합 신고 페이지 - 검색 기능
// ========================================
if (location.href.includes("/admin/report")) {
    document.addEventListener("DOMContentLoaded", () => {
        
        // 오늘 신고 카운트
        const loadReportStats = () => {
            fetch("/admin/stats")
            .then(resp => resp.json())
            .then(data => {
                const todayReport = document.getElementById("todayReport");

                if (todayReport && todayReport.innerText != data.todayReportCount) {
                    todayReport.style.opacity = 0;
                    setTimeout(() => {
                        todayReport.innerText = data.todayReportCount;
                        todayReport.style.opacity = 1;
                    }, 200);
                }
            })
            .catch(err => console.error("신고된 수 불러오기 오류:", err));
        };

        loadReportStats();
        setInterval(loadReportStats, 10 * 60 * 1000);

        // 초기화 버튼
        const resetBtn = document.getElementById("resetBtn");
        if (resetBtn) {
            resetBtn.addEventListener("click", () => {
                location.href = "/admin/report";
            });
        }

        // 검색 폼 제출 전 처리
        const searchArea = document.getElementById("search-area");
        if (searchArea) {
            searchArea.addEventListener("submit", (e) => {
                const keyword = document.getElementById("search-query")?.value.trim() || '';
                const reportType = document.getElementById("reportTypeSelect")?.value || '';
                const reportStatus = document.getElementById("reportStatusSelect")?.value || '';
                const startDate = document.getElementById("startDate")?.value || '';
                const endDate = document.getElementById("endDate")?.value || '';
                
                // 모든 필드가 비어있는 경우만 경고
                if (!keyword && !reportType && !reportStatus && !startDate && !endDate) {
                    e.preventDefault();
                    alert("검색어를 입력하거나 필터를 선택하세요.");
                    return false;
                }
            });
        }

        // 전체선택
        const checkAll = document.getElementById("checkAll");
        if (checkAll) {
            checkAll.addEventListener("change", function () {
                const checked = this.checked;
                document.querySelectorAll("input[name='reportNo']").forEach(cb => cb.checked = checked);
            });
        }

        // 처리완료 버튼 클릭
        const completeBtn = document.getElementById("completeBtn");
        if (completeBtn) {
            completeBtn.addEventListener("click", () => {
                const checked = document.querySelectorAll("input[name='reportNo']:checked");

                if (checked.length === 0) {
                    alert("처리할 신고를 선택하세요.");
                    return;
                }

                // 선택된 신고번호와 각 처리 결과를 모아 배열로 만듦
                const reportList = Array.from(checked).map(cb => {
                    const reportNo = cb.value;
                    const select = document.querySelector(`#result-${reportNo}`);
                    const resultType = select ? select.value : "-";
                    return { reportNo, resultType };
                });

                console.log(reportList);

                // fetch로 백엔드 전송
                fetch("/admin/report/updateResult", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(reportList)
                })
                .then(res => res.text())
                .then(result => {
                    if (result === "success") {
                        alert("처리 완료되었습니다!");
                        location.reload();
                    } else {
                        alert("처리 실패!");
                    }
                })
                .catch(err => console.error(err));
            });
        }
    });
}

// ========================================
// ✅ 회원 조회 페이지 - 검색 기능
// ========================================
if (location.href.includes("/admin/main")) {
    document.addEventListener("DOMContentLoaded", () => {
        
        // 통계 조회
        const loadStats = () => {
            fetch("/admin/stats")
            .then(resp => resp.json())
            .then(data => {
                const todayJoin = document.getElementById("todayJoin");
                const totalUser = document.getElementById("totalUser");

                if (todayJoin && todayJoin.innerText != data.todayJoinCount) {
                    todayJoin.style.opacity = 0;
                    setTimeout(() => {
                        todayJoin.innerText = data.todayJoinCount;
                        todayJoin.style.opacity = 1;
                    }, 200);
                }

                if (totalUser && totalUser.innerText != data.totalUserCount) {
                    totalUser.style.opacity = 0;
                    setTimeout(() => {
                        totalUser.innerText = data.totalUserCount;
                        totalUser.style.opacity = 1;
                    }, 200);
                }
            })
            .catch(err => console.error("통계 불러오기 오류:", err));
        };
        
        loadStats();
        setInterval(loadStats, 10 * 60 * 1000);

        // 초기화 버튼
        const resetBtn = document.getElementById("resetBtn");
        if (resetBtn) {
            resetBtn.addEventListener("click", () => {
                location.href = "/admin/main";
            });
        }

        // 검색 폼 제출 전 처리
        const searchArea = document.getElementById("search-area");
        if (searchArea) {
            searchArea.addEventListener("submit", (e) => {
                const keyword = document.getElementById("search-query").value.trim();
                const checkboxes = document.querySelectorAll('input[name="searchType"]:checked');
                
                if (!keyword && checkboxes.length === 0) {
                    e.preventDefault();
                    alert("검색어를 입력하거나 상세검색 옵션을 선택하세요.");
                    return false;
                }
            });
        }
    });
}

// ========================================
// ✅ 상품 관리 (최종 수정)
// ========================================
if (location.href.includes("/admin/goods")) {
    document.addEventListener("DOMContentLoaded", () => {
        
        // 초기화 버튼
        const resetBtn = document.getElementById("resetBtn");
        if (resetBtn) {
            resetBtn.addEventListener("click", () => {
                location.href = "/admin/goods";
            });
        }

        // 날짜 빠른 선택 버튼
        const dateBtns = document.querySelectorAll(".dateBtn");
        const startDateInput = document.getElementById("startDate");
        const endDateInput = document.getElementById("endDate");

        dateBtns.forEach(btn => {
            btn.addEventListener("click", () => {
                const period = btn.getAttribute("data-period");
                const today = new Date();
                const endDate = today.toISOString().split('T')[0];
                
                let startDate;
                if (period === "1month") {
                    const oneMonthAgo = new Date(today.setMonth(today.getMonth() - 1));
                    startDate = oneMonthAgo.toISOString().split('T')[0];
                } else if (period === "1year") {
                    const oneYearAgo = new Date(today.setFullYear(today.getFullYear() - 1));
                    startDate = oneYearAgo.toISOString().split('T')[0];
                } else if (period === "all") {
                    startDate = "";
                    endDateInput.value = "";
                    return;
                }
                
                startDateInput.value = startDate;
                endDateInput.value = endDate;
            });
        });

        // 전체 선택 체크박스
        const checkAllGoods = document.getElementById("checkAllGoods");
        const goodsCheckboxes = document.querySelectorAll(".goods-checkbox");
        const selectedCount = document.getElementById("selectedCount");

        if (checkAllGoods) {
            checkAllGoods.addEventListener("change", function() {
                const checked = this.checked;
                goodsCheckboxes.forEach(cb => cb.checked = checked);
                updateSelectedCount();
            });
        }

        // 개별 체크박스 변경 시
        goodsCheckboxes.forEach(cb => {
            cb.addEventListener("change", updateSelectedCount);
        });

        // 선택 개수 업데이트
        function updateSelectedCount() {
            const count = document.querySelectorAll(".goods-checkbox:checked").length;
            selectedCount.textContent = `${count}개 선택됨`;
        }

        // 초기 선택 개수 표시
        updateSelectedCount();

        // 선택 상품 삭제
        const deleteBtn = document.getElementById("deleteSelectedBtn");
        if (deleteBtn) {
            deleteBtn.addEventListener("click", () => {
                const checked = document.querySelectorAll(".goods-checkbox:checked");
                
                if (checked.length === 0) {
                    alert("삭제할 상품을 선택하세요.");
                    return;
                }

                if (!confirm(`선택한 ${checked.length}개 상품을 삭제하시겠습니까?`)) {
                    return;
                }

                const boardNos = Array.from(checked).map(cb => cb.value);

                fetch("/admin/goods/delete", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(boardNos)
                })
                .then(res => res.text())
                .then(result => {
                    if (result === "success") {
                        alert("상품이 삭제되었습니다!");
                        location.reload();
                    } else {
                        alert("삭제 실패!");
                    }
                })
                .catch(err => console.error(err));
            });
        }

        // SOLD-OUT 버튼
        const soldOutBtn = document.getElementById("soldOutBtn");
        if (soldOutBtn) {
            soldOutBtn.addEventListener("click", () => {
                const checked = document.querySelectorAll(".goods-checkbox:checked");
                
                if (checked.length === 0) {
                    alert("품절 처리할 상품을 선택하세요.");
                    return;
                }

                if (!confirm(`선택한 ${checked.length}개 상품을 품절 처리하시겠습니까?`)) {
                    return;
                }

                const boardNos = Array.from(checked).map(cb => cb.value);

                fetch("/admin/goods/soldout", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(boardNos)
                })
                .then(res => res.text())
                .then(result => {
                    if (result === "success") {
                        alert("품절 처리되었습니다!");
                        location.reload();
                    } else {
                        alert("품절 처리 실패!");
                    }
                })
                .catch(err => console.error(err));
            });
        }

        // 재입고 버튼
        const restockBtn = document.getElementById("restockBtn");
        if (restockBtn) {
            restockBtn.addEventListener("click", () => {
                const checked = document.querySelectorAll(".goods-checkbox:checked");
                
                if (checked.length === 0) {
                    alert("재입고할 상품을 선택하세요.");
                    return;
                }

                const stockInput = prompt(`재입고 수량을 입력하세요 (숫자만):`, "10");
                
                if (stockInput === null) return;
                
                const stock = parseInt(stockInput);
                
                if (isNaN(stock) || stock <= 0) {
                    alert("올바른 수량을 입력하세요.");
                    return;
                }

                if (!confirm(`선택한 ${checked.length}개 상품을 ${stock}개씩 재입고하시겠습니까?`)) {
                    return;
                }

                const boardNos = Array.from(checked).map(cb => cb.value);
                const data = {
                    boardNos: boardNos,
                    stock: stock
                };

                fetch("/admin/goods/restock", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(data)
                })
                .then(res => res.text())
                .then(result => {
                    if (result === "success") {
                        alert("재입고되었습니다!");
                        location.reload();
                    } else {
                        alert("재입고 실패!");
                    }
                })
                .catch(err => console.error(err));
            });
        }

        // ✅ 정렬 변경 (드롭다운 선택 유지)
        const sortSelect = document.getElementById("sortSelect");
        if (sortSelect) {
            sortSelect.addEventListener("change", (e) => {
                const sort = e.target.value;
                const urlParams = new URLSearchParams(window.location.search);
                
                // sort 값 설정
                if (sort) {
                    urlParams.set('sort', sort);
                } else {
                    urlParams.delete('sort');
                }
                
                // 1페이지로 이동
                urlParams.set('cp', '1');
                
                // URL 생성 및 이동
                location.href = `/admin/goods?${urlParams.toString()}`;
            });
        }

        // ✅ 페이지네이션 버튼 재정의 (정렬 파라미터 유지)
        const paginationButtons = document.querySelectorAll('.pagination button');
        paginationButtons.forEach(btn => {
            // 기존 onclick 제거하고 새로 정의
            btn.removeAttribute('onclick');
            
            btn.addEventListener('click', function(e) {
                e.preventDefault();
                
                // disabled 버튼은 무시
                if (this.disabled) return;
                
                // 버튼에서 페이지 번호 추출
                let cp;
                if (this.classList.contains('pagination-btn')) {
                    // 이전/다음 버튼
                    const img = this.querySelector('img');
                    if (img && img.src.includes('previous')) {
                        // 이전 버튼
                        const urlParams = new URLSearchParams(window.location.search);
                        const currentPage = parseInt(urlParams.get('cp') || '1');
                        cp = Math.max(1, currentPage - 1);
                    } else if (img && img.src.includes('next')) {
                        // 다음 버튼
                        const urlParams = new URLSearchParams(window.location.search);
                        const currentPage = parseInt(urlParams.get('cp') || '1');
                        cp = currentPage + 1;
                    }
                } else {
                    // 페이지 번호 버튼
                    cp = this.textContent.trim();
                }
                
                if (!cp) return;
                
                // 현재 URL의 모든 파라미터 가져오기
                const urlParams = new URLSearchParams(window.location.search);
                urlParams.set('cp', cp);
                
                // 페이지 이동
                location.href = `/admin/goods?${urlParams.toString()}`;
            });
        });
    });
}




// ========================================
// ✅ 거래 내역
// ========================================
if (location.href.includes("/admin/order")) {
    document.addEventListener("DOMContentLoaded", () => {
        
        // 초기화 버튼
        const resetBtn = document.getElementById("resetBtn");
        if (resetBtn) {
            resetBtn.addEventListener("click", () => {
                location.href = "/admin/order";
            });
        }

        // 날짜 빠른 선택 버튼 (수정됨)
        const dateBtns = document.querySelectorAll(".dateBtn");
        const startDateInput = document.getElementById("startDate");
        const endDateInput = document.getElementById("endDate");

        dateBtns.forEach(btn => {
            btn.addEventListener("click", () => {
                const period = btn.getAttribute("data-period");
                const today = new Date();
                
                let startDate = "";
                let endDate = "";
                
                if (period === "1month") {
                    const oneMonthAgo = new Date(today.setMonth(today.getMonth() - 1));
                    startDate = oneMonthAgo.toISOString().split('T')[0];
                    endDate = new Date().toISOString().split('T')[0];
                } else if (period === "1year") {
                    const oneYearAgo = new Date(today.setFullYear(today.getFullYear() - 1));
                    startDate = oneYearAgo.toISOString().split('T')[0];
                    endDate = new Date().toISOString().split('T')[0];
                }
                // period === "all"이면 빈 문자열 유지
                
                startDateInput.value = startDate;
                endDateInput.value = endDate;
            });
        });

        // 정렬 변경
        const sortSelect = document.getElementById("sortSelect");
        if (sortSelect) {
            sortSelect.addEventListener("change", (e) => {
                const sort = e.target.value;
                const urlParams = new URLSearchParams(window.location.search);
                
                if (sort) {
                    urlParams.set('sort', sort);
                } else {
                    urlParams.delete('sort');
                }
                
                urlParams.set('cp', '1');
                location.href = `/admin/order?${urlParams.toString()}`;
            });
        }

        // 페이지네이션 버튼 재정의
        const paginationButtons = document.querySelectorAll('.pagination button');
        paginationButtons.forEach(btn => {
            btn.removeAttribute('onclick');
            
            btn.addEventListener('click', function(e) {
                e.preventDefault();
                
                if (this.disabled) return;
                
                let cp;
                if (this.classList.contains('pagination-btn')) {
                    const img = this.querySelector('img');
                    if (img && img.src.includes('previous')) {
                        const urlParams = new URLSearchParams(window.location.search);
                        const currentPage = parseInt(urlParams.get('cp') || '1');
                        cp = Math.max(1, currentPage - 1);
                    } else if (img && img.src.includes('next')) {
                        const urlParams = new URLSearchParams(window.location.search);
                        const currentPage = parseInt(urlParams.get('cp') || '1');
                        cp = currentPage + 1;
                    }
                } else {
                    cp = this.textContent.trim();
                }
                
                if (!cp) return;
                
                const urlParams = new URLSearchParams(window.location.search);
                urlParams.set('cp', cp);
                
                location.href = `/admin/order?${urlParams.toString()}`;
            });
        });
    });
}




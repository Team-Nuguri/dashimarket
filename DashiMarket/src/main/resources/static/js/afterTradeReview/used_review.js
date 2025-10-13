// 별점 기능
    const ratings = {
      rating1: { stars: null, text: null, messages: ['다시 거래하고 싶진 않아요', '약간 아쉬운 거래였어요', '그냥 무난했어요', '기분 좋은 거래였어요', '정말 최고의 거래였어요'] },
      rating2: { stars: null, text: null, messages: ['약속을 지키지 않으셨어요', '사전 연락 없이 늦으셨어요', '조금 늦으셨지만 미리 연락 주셨어요', '정시에 맞춰주셨어요', '약속 시간보다 일찍 와주셨어요'] },
      rating3: { stars: null, text: null, messages: ['상태가 많이 달라 실망했어요', '설명과 다른 부분이 있었어요', '사용감이 조금 더 있었지만 괜찮았어요', '설명과 거의 같았어요', '설명보다 더 좋은 상태였어요'] },
      rating4: { stars: null, text: null, messages: ['다시 거래하고 싶지 않아요', '특별히 다시 하고 싶진 않아요', '딱히 상관없어요', '기회가 되면 또 하고 싶어요', '꼭 다시 거래하고 싶어요'] }
    };

    Object.keys(ratings).forEach(ratingId => {
      const container = document.getElementById(ratingId);
      const stars = container.querySelectorAll(`.rating__star${ratingId.slice(-1)}`);
      const textElement = document.getElementById(`selected-${ratingId}`);
      
      const hiddenInput = document.querySelector(`#uploadForm input[name="${ratingId}"]`);
      
      ratings[ratingId].stars = stars;
      ratings[ratingId].text = textElement;

      stars.forEach(star => {
        star.addEventListener('click', function() {
          const rating = parseInt(this.getAttribute('data-rating'));
          
          stars.forEach((s, index) => {
            if (index < rating) {
              s.classList.remove('far');
              s.classList.add('fas');
            } else {
              s.classList.remove('fas');
              s.classList.add('far');
            }
          });
          
          textElement.textContent = ratings[ratingId].messages[rating - 1];
          
          if (hiddenInput) {
             hiddenInput.value = rating;
          }
          
        });

        star.addEventListener('mouseenter', function() {
          const rating = parseInt(this.getAttribute('data-rating'));
          stars.forEach((s, index) => {
            if (index < rating) {
              s.style.opacity = '0.7';
            }
          });
        });

        star.addEventListener('mouseleave', function() {
          stars.forEach(s => {
            s.style.opacity = '1';
          });
        });
      });
    });

    // 이미지 업로드 미리보기
    document.getElementById('image').addEventListener('change', function(e) {
      const preview = document.getElementById('preview');
      const file = e.target.files[0];
      
      if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
          preview.innerHTML = `<img src="${e.target.result}" alt="미리보기">`;
        };
        reader.readAsDataURL(file);
      }
    });

    // 폼 제출
    document.getElementById('uploadForm').addEventListener('submit', function(e) {
      e.preventDefault();
      alert('거래 후기가 제출되었습니다!');
    });
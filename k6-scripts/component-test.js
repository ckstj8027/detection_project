import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomString } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export const options = {
  stages: [
    { duration: '10s', target: 100 }, // 10초 동안 100명까지 증가r

    { duration: '50s', target: 500 }, // 50초 동안 500명 유지
    { duration: '10s', target: 0 },   // 10초 동안 종료
  ],
};

export default function () {
  // 매우 긴 문장 생성 (약 200자)
  const longText = `This is a very long text to test the performance of the trigram index in PostgreSQL. We want to see how fast it can detect the word badword inside this long paragraph of meaningless text. ${randomString(100)}`;
  
  // URL Encoding 필요
  const encodedText = encodeURIComponent(longText);

  const res = http.get(`http://localhost:8081/test/profanity?text=${encodedText}`, {
    tags: { name: '/test/profanity' }, // 이 URL들은 모두 이 이름으로 합쳐서 통계 내줘!
  });

  check(res, {
    'is status 200': (r) => r.status === 200,
    'is profane detected': (r) => r.body === 'true', // badword가 포함되어 있으므로 true여야 함
    'response time < 100ms': (r) => r.timings.duration < 100,
  });

  sleep(0.5); // 0.5초마다 요청 (매우 빠른 속도로 부하)
}

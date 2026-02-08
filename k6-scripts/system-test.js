import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomString } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export const options = {
  stages: [
    { duration: '30s', target: 200 }, // 30초 동안 200명까지 서서히 증가 (Ramp-up)
    { duration: '4m', target: 1000 }, // 4분 동안 1000명 유지 (Load)
    { duration: '30s', target: 0 },   // 30초 동안 0명으로 감소 (Ramp-down)
  ],
};

export default function () {
  const isBad = Math.random() < 0.3; // 30% 확률로 욕설 포함
  const content = isBad ? `This is a badword test ${randomString(10)}` : `Clean content ${randomString(20)}`;

  const payload = JSON.stringify({
    title: `Test Post ${randomString(5)}`,
    content: content,
    author: `user_${randomString(5)}`,
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.post('http://localhost:8080/posts', payload, params);

  check(res, {
    'is status 200': (r) => r.status === 200,
    'response time < 200ms': (r) => r.timings.duration < 200,
  });

  sleep(1); // 각 유저는 1초에 한 번씩 요청 (Think time)
}

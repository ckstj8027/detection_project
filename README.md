<img width="1022" height="270" alt="image" src="https://github.com/user-attachments/assets/fdc91ec0-8335-4bac-98c3-ab6f74c96a3b" />

욕설 처리 로직 구현
 게시물 작성시 Queue에 넣어두고 최대한 빠르게 is_visible을 false로 처리해서 사용자에게 보여주지 않는 방식


프로젝트 설계
커뮤니티 서버 : 사용자가 게시물과 댓글을 작성
관리자 서버와 RabbitMQ를 통해 연결
관리자 서버 : 금지어를 관리하고 외부 API 또는 다른 팀의 AI를 통해서 욕설을 탐지
커뮤니티 서버와 RabbitMQ로 연결되있고 욕설 탐지 서버와 gRPC로 연결

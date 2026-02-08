CREATE TABLE IF NOT EXISTS profanity_word (
    id BIGSERIAL PRIMARY KEY,
    word VARCHAR(255) NOT NULL
);

-- Insert some sample bad words
INSERT INTO profanity_word (word) VALUES ('badword'), ('abuse'), ('spam');

-- [데이터 뻥튀기] 10만 건의 더미 데이터 자동 생성
INSERT INTO profanity_word (word)
SELECT 'dummy_word_' || i
FROM generate_series(1, 100000) AS i;

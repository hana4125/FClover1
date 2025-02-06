DESC TABLE search_log;

SELECT * FROM search_log;


CREATE TABLE search_log (
    search_log_no       BIGINT AUTO_INCREMENT,
    search_keyword      VARCHAR(255) NOT NULL,
    member_no           BIGINT NULL,
    member_age_range    VARCHAR(20) NULL,
    member_gender       ENUM('M', 'F', 'N') NOT NULL DEFAULT 'N',
    session_id          VARCHAR(255) NOT NULL,
    search_datetime     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- 파티셔닝용 생성 컬럼 YYYYMM 형식
    search_date_ym      INT GENERATED ALWAYS AS (YEAR(search_datetime) * 100 + MONTH(search_datetime)) STORED,

    -- 인덱스 설정
    KEY idx_search_keyword (search_keyword),
    KEY idx_search_datetime (search_datetime),

    PRIMARY KEY (search_log_no, search_datetime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
    -- 파티셔닝 설정
PARTITION BY RANGE (search_date_ym) (
    PARTITION p202502 VALUES LESS THAN (202503),
    PARTITION p202503 VALUES LESS THAN (202504),
    PARTITION pMax    VALUES LESS THAN (MAXVALUE)
);


-- 매월 1일에 다음달 로그 테이블 파티셔닝 추가하는 MySQL 이벤트 스케쥴러
DELIMITER $$

CREATE EVENT IF NOT EXISTS add_next_month_partition
    ON SCHEDULE EVERY 1 MONTH
        STARTS '2025-03-01 00:00:00'
    DO
    BEGIN
        DECLARE next_partition_value INT;
        DECLARE next_partition_upper_bound INT;
        DECLARE next_partition_name VARCHAR(20);

        -- 현재 날짜 기준으로 다음 달의 연도와 월을 YYYYMM 정수 형식으로 계산
        SET next_partition_value = CAST(DATE_FORMAT(DATE_ADD(CURRENT_DATE, INTERVAL 1 MONTH), '%Y%m') AS UNSIGNED);

        -- 파티션 정의는 VALUES LESS THAN이므로, 다음 달의 값에 1을 더한 값을 경계값으로 설정합니다.
        SET next_partition_upper_bound = next_partition_value + 1;

        -- 파티션 이름은 'p' 접두어와 YYYYMM 값을 결합합니다.
        SET next_partition_name = CONCAT('p', next_partition_value);

        SET @sql_text = CONCAT(
                'ALTER TABLE search_log ADD PARTITION (PARTITION ', next_partition_name,
                ' VALUES LESS THAN (', next_partition_upper_bound, '))'
                        );

        PREPARE stmt FROM @sql_text;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END$$

DELIMITER ;



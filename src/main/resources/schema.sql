DROP TABLE IF EXISTS wallethub.log_data;

CREATE TABLE wallethub.log_data (
    id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    logDate DATETIME NOT NULL,
    request VARCHAR(20) NOT NULL,
    ipValue VARCHAR(20) NOT NULL,
    status INT(3) NOT NULL,
    uAgent VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS wallethub.result (
    id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    ipValue VARCHAR(20) NOT NULL,
    comment VARCHAR(1000) NOT NULL
);
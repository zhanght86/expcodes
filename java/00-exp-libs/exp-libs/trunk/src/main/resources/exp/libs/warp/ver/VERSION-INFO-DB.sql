
DROP TABLE IF EXISTS T_PROJECT_INFO;
CREATE TABLE IF NOT EXISTS T_PROJECT_INFO (
  I_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
  S_PROJECT_NAME  TEXT(128)  NULL,
  S_PROJECT_DESC  TEXT(256)  NULL,
  S_TEAM_NAME  TEXT(64)  NULL,
  S_PROJECT_CHARSET  TEXT(32)  NULL,
  S_DISK_SIZE  TEXT(32)  NULL,
  S_CACHE_SIZE  TEXT(32)  NULL,
  S_APIS  TEXT(64)  NULL
);

DROP TABLE IF EXISTS T_HISTORY_VERSIONS;
CREATE TABLE IF NOT EXISTS T_HISTORY_VERSIONS (
  I_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
  S_AUTHOR  TEXT(64)  NULL,
  S_VERSION  TEXT(64)  NULL,
  S_DATETIME  TEXT(32)  NULL,
  S_UPGRADE_CONTENT  TEXT(1024)  NULL, 
  S_UPGRADE_STEP  TEXT(1024)  NULL
);

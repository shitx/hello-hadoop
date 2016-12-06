--本文主要讲述如何优化mysql数据库
-- 1 开启慢查询日志【发现有问题的sql查询】
show variables like "slow_query_log";
show variables like "%log%";
set global log_queries_not_using_indexes=on;
show variables like 'long_query_time'; --慢查询的时间范围
set global long_query_time=0; --重开一个窗口生效
set global slow_query_log=on;
show variables like 'slow%';
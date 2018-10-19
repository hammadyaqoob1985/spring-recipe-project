CREATE DATABASE sfg_dev;
CREATE DATABASE sfg_prod;

#Create database service accounts
CREATE USER 'sfg_dev_user'@'127.0.0.1' IDENTIFIED BY 'password';
CREATE USER 'sfg_prod_user'@'127.0.0.1' IDENTIFIED BY 'password';
CREATE USER 'sfg_dev_user'@'%' IDENTIFIED BY 'password';
CREATE USER 'sfg_prod_user'@'%' IDENTIFIED BY 'password';

#Database grants
GRANT SELECT ON sfg_dev.* to 'sfg_dev_user'@'127.0.0.1';
GRANT INSERT ON sfg_dev.* to 'sfg_dev_user'@'127.0.0.1';
GRANT DELETE ON sfg_dev.* to 'sfg_dev_user'@'127.0.0.1';
GRANT UPDATE ON sfg_dev.* to 'sfg_dev_user'@'127.0.0.1';
GRANT SELECT ON sfg_prod.* to 'sfg_prod_user'@'127.0.0.1';
GRANT INSERT ON sfg_prod.* to 'sfg_prod_user'@'127.0.0.1';
GRANT DELETE ON sfg_prod.* to 'sfg_prod_user'@'127.0.0.1';
GRANT UPDATE ON sfg_prod.* to 'sfg_prod_user'@'127.0.0.1';
GRANT SELECT ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT INSERT ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT DELETE ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT UPDATE ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT SELECT ON sfg_prod.* to 'sfg_prod_user'@'%';
GRANT INSERT ON sfg_prod.* to 'sfg_prod_user'@'%';
GRANT DELETE ON sfg_prod.* to 'sfg_prod_user'@'%';
GRANT UPDATE ON sfg_prod.* to 'sfg_prod_user'@'%';


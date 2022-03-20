create database ribbontek;
BEGIN;
create user template_api with password 'template_api';
grant connect on database ribbontek to template_api;
grant all privileges on database ribbontek to postgres;
COMMIT;

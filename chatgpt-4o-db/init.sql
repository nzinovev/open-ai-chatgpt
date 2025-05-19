-- Create application role with login
CREATE ROLE chatgpt_4o_app WITH
  LOGIN
  PASSWORD 'strongPassword';

-- Create schema
CREATE SCHEMA chatgpt_4o AUTHORIZATION chatgpt_4o_app;

-- Grant all privileges on the schema to the role
GRANT ALL PRIVILEGES ON SCHEMA chatgpt_4o TO chatgpt_4o_app;

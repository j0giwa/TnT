--TRUNCATE TABLE USERS RESTART IDENTITY;
--TRUNCATE TABLE GROUPS RESTART IDENTITY;

INSERT INTO GROUPS(name, admin) VALUES ('user', false);
INSERT INTO GROUPS(name, admin) VALUES ('admin', true);

INSERT INTO USERS(firstname, lastname, username, email, password, group_id) 
VALUES 
    ('Rüdiger', 'Rüdiger', 'Ruediger', 'ruediger@thowl.de', '$2a$15$q/VCgMfG.XLG55DT2dgehOVFcAMgoV2iwgB7AA2yhzpk53E6uJ.La', 0),
    ('Ragnar', 'Lothbrock', 'Ragnar', 'ragnar@thowl.de', '$2y$15$zsSW05QfB1XpwL61lq9T9uhIHqZ7YzglIfwpFm8qd5WBl/nUXkj76', 0);

INSERT INTO
    groups(id, name, admin)
VALUES
    (0, 'user', false),
    (2, 'admin', true);

INSERT INTO
    users(id, username, email, password, group_id)
VALUES
    (0, 'Ruediger', 'ruediger@thowl.de', '$2a$15$q/VCgMfG.XLG55DT2dgehOVFcAMgoV2iwgB7AA2yhzpk53E6uJ.La', 0),
    (1, 'Ragnar', 'ragnar@thowl.de', '$2y$15$zsSW05QfB1XpwL61lq9T9uhIHqZ7YzglIfwpFm8qd5WBl/nUXkj76', 0);

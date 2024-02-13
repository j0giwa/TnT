INSERT INTO
    groups(id, name, admin)
VALUES
    (1, 'tester', false),
    (2, 'user', false),
    (3, 'admin', true);

INSERT INTO
    users(id, username, email, password, group_id)
VALUES
    (0, 'Rüdiger', 'ruediger@thowl.de', '$2a$15$q/VCgMfG.XLG55DT2dgehOVFcAMgoV2iwgB7AA2yhzpk53E6uJ.La', 1);

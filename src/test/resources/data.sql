INSERT INTO 
	users(username, firstname, lastname, email, password, api_token, group_id) 
VALUES 
	('Rüdiger', 'Rüdiger', 'Schlablonzki', 'ruediger@thowl.de', '$2a$15$D3zQTzUx12LgCOHWOH8H4eSXbZ/lhO.l46wpR8vrA6qZ/uhOct..W', 'f7c7699e-6515-447e-9226-6c4d3fae4bfb', 1),
	('Tasktester', 'Task', 'Tester', 'ttest@thowl.de', '$2a$15$D3zQTzUx12LgCOHWOH8H4eSXbZ/lhO.l46wpR8vrA6qZ/uhOct..W', 'f7c7699e-6515-447e-9226-6c4d3fae4bfb', 1),
	('Notestester', 'Notes', 'Tester', 'ntest@thowl.de', '$2a$15$D3zQTzUx12LgCOHWOH8H4eSXbZ/lhO.l46wpR8vrA6qZ/uhOct..W', 'f7c7699e-6515-447e-9226-6c4d3fae4bfb', 1);

INSERT INTO 
	tasks(content, created_at, name, done, due_date, overdue, priority, time, user_id) 
VALUES 
	('A simple test task', '2024-03-03 21:07:44.000000', 'A test task', false, '2002-07-20 00:00:00.000000', false, 0, '1970-01-01 00:30:00.000000', 1),
	('Another simple test task', '2024-03-04 21:07:44.000000', 'Another test task', false, '2002-07-20 00:00:00.000000', false, 0, '1970-01-01 00:30:00.000000', 1);;

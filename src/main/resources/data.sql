TRUNCATE table tenants.tenants, cust_jane.notes, cust_john.notes, shared.note_type RESTART IDENTITY CASCADE;

INSERT INTO tenants.tenants (schema, login, password) VALUES ('cust_john', 'john', 'john');
INSERT INTO tenants.tenants (schema, login, password) VALUES ('cust_jane', 'jane', 'jane');

INSERT INTO shared.note_type (type) VALUES ('todo');
INSERT INTO shared.note_type (type) VALUES ('note');

INSERT INTO cust_jane.notes (note, note_type) VALUES ('Note by Jane', (select id from shared.note_type where type = 'note'));
INSERT INTO cust_jane.notes (note, note_type) VALUES ('2nd note by Jane', (select id from shared.note_type where type = 'note'));

INSERT INTO cust_john.notes (note, note_type) VALUES ('Note by John', (select id from shared.note_type where type = 'note'));
INSERT INTO cust_john.notes (note, note_type) VALUES ('2nd note by John', (select id from shared.note_type where type = 'note'));

DROP SCHEMA IF EXISTS pae CASCADE;
CREATE SCHEMA pae;

CREATE TABLE pae.users
(
    user_idUser       SERIAL PRIMARY KEY,
    user_lastname     TEXT    NOT NULL,
    user_firstname    TEXT    NOT NULL,
    user_email        TEXT    NOT NULL,
    user_password     TEXT    NOT NULL,
    user_phoneNumber  TEXT    NOT NULL,
    user_registerDate date    NOT NULL,
    user_role         TEXT    NOT NULL,
    user_academicYear TEXT    NULL,
    user_profilePicture TEXT NULL,
    user_version      INTEGER NOT NULL
);

CREATE TABLE pae.companies
(
    company_idCompany           SERIAL PRIMARY KEY,
    company_tradeName           TEXT    NOT NULL,
    company_designation         TEXT    NULL,
    company_address             TEXT    NOT NULL,
    company_city                TEXT    NULL,
    company_phoneNumber         TEXT    NULL,
    company_email               TEXT    NULL,
    company_blacklisted         BOOLEAN NOT NULL,
    company_blacklistMotivation TEXT    NULL,
    company_version             INTEGER NOT NULL
);

CREATE TABLE pae.contacts
(
    contact_idContact     SERIAL PRIMARY KEY,
    contact_idCompany     INTEGER REFERENCES pae.companies (company_idcompany) NOT NULL,
    contact_idStudent     INTEGER REFERENCES pae.users (user_iduser)           NOT NULL,
    contact_state         TEXT                                                 NOT NULL,
    contact_meetPlace     TEXT                                                 NULL,
    contact_refusalReason TEXT                                                 NULL,
    contact_academicYear  TEXT                                                 NOT NULL,
    contact_version       INTEGER                                              NOT NULL
);

CREATE TABLE pae.internshipSupervisors
(
    internshipSupervisor_idInternshipSupervisor SERIAL PRIMARY KEY,
    internshipSupervisor_lastname               TEXT                                                 NOT NULL,
    internshipSupervisor_firstname              TEXT                                                 NOT NULL,
    internshipSupervisor_phoneNumber            TEXT                                                 NOT NULL,
    internshipSupervisor_email                  TEXT                                                 NULL,
    internshipSupervisor_idCompany              INTEGER REFERENCES pae.companies (company_idcompany) NOT NULL,
    internshipSupervisor_version                INTEGER                                              NOT NULL
);

CREATE TABLE pae.internships
(
    internship_idInternship           SERIAL PRIMARY KEY,
    internship_idStudent              INTEGER REFERENCES pae.users (user_iduser)                                                 NOT NULL,
    internship_internshipProject      TEXT                                                                                       NULL,
    internship_signatureDate          DATE                                                                                       NOT NULL,
    internship_idContact              INTEGER REFERENCES pae.contacts (contact_idContact)                                        NOT NULL,
    internship_idInternshipSupervisor INTEGER REFERENCES pae.internshipSupervisors (internshipSupervisor_idInternshipSupervisor) NOT NULL,
    internship_idCompany              INTEGER REFERENCES pae.companies (company_idcompany)                                       NOT NULL,
    internship_version                INTEGER                                                                                    NOT NULL
);

-- Insertion de données dans la table entreprises
INSERT INTO pae.companies (company_tradeName, company_address, company_city, company_phoneNumber, company_version,
                           company_blacklisted)
VALUES ('Assyst Europe', 'Avenue du Japon, 1/B9', '1420 Braine-l''Alleud', '02.609.25.00', 1, false);

INSERT INTO pae.companies (company_tradeName, company_address, company_city, company_phoneNumber, company_version,
                           company_blacklisted)
VALUES ('LetsBuild', 'Chaussée de Bruxelles, 135A', '1310 La Hulpe', '014 54 67 54', 1, false);

INSERT INTO pae.companies (company_tradeName, company_address, company_city, company_phoneNumber, company_version,
                           company_blacklisted)
VALUES ('Niboo', 'Boulevard du Souverain, 24', '1170 Watermael-Boitsfort', '0487 02 79 13', 1, false);

INSERT INTO pae.companies (company_tradeName, company_address, company_city, company_phoneNumber, company_version,
                           company_blacklisted)
VALUES ('Sopra Steria', 'Avenue Arnaud Fraiteur, 15/23', '1050 Bruxelles', '02 566 66 66', 1, false);


-- Insertion de données dans la table responsables
INSERT INTO pae.internshipSupervisors (internshipSupervisor_lastname, internshipSupervisor_firstname,
                                       internshipSupervisor_phoneNumber, internshipSupervisor_email,
                                       internshipSupervisor_idCompany, internshipSupervisor_version)
VALUES ('Dossche', 'Stéphanie', '014.54.67.54', 'stephanie.dossche@letsbuild.com', 2, 1);

INSERT INTO pae.internshipSupervisors (internshipSupervisor_lastname, internshipSupervisor_firstname,
                                       internshipSupervisor_phoneNumber, internshipSupervisor_idCompany,
                                       internshipSupervisor_version)
VALUES ('ALVAREZ CORCHETE', 'Roberto', '02.566.60.14', 4, 1);

INSERT INTO pae.internshipSupervisors (internshipSupervisor_lastname, internshipSupervisor_firstname,
                                       internshipSupervisor_phoneNumber, internshipSupervisor_email,
                                       internshipSupervisor_idCompany, internshipSupervisor_version)
VALUES ('Assal', 'Farid', '0474 39 69 09', 'f.assal@assyst-europe.Com', 1, 1);


-- Insertion de données dans la table utilisateurs
INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Baroni', 'Raphaël', 'raphael.baroni@vinci.be', '$2a$10$Pd7afEGz1wpWEJjNVz14veBc4uSClSbOlCmDJ0FVNTG.I.GzUpZk.',
        '0481 01 01 01', '21-09-20', 'TEACHER', '2020-2021', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Lehmann', 'Brigitte', 'brigitte.lehmann@vinci.be',
        '$2a$10$Pd7afEGz1wpWEJjNVz14veBc4uSClSbOlCmDJ0FVNTG.I.GzUpZk.', '0482 02 02 02', '21-09-20', 'TEACHER',
        '2020-2021', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Leleux', 'Laurent', 'laurent.leleux@vinci.be', '$2a$10$Pd7afEGz1wpWEJjNVz14veBc4uSClSbOlCmDJ0FVNTG.I.GzUpZk.',
        '0483 03 03 03', '21-09-20', 'TEACHER', '2020-2021', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Lancaster', 'Annouck', 'annouck.lancaster@vinci.be',
        '$2a$10$XaYjjV5Qk4.wJcOfB5NmfeDLN6eXt3WzMw97JMf6LD7oHcbvLzqb6', '0484 04 04 04', '21-09-20', 'ADMIN',
        '2020-2021', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Line', 'Caroline', 'Caroline.line@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0486 00 00 01', '18-09-23', 'STUDENT',
        '2023-2024', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Ile', 'Achille', 'Ach.ile@student.vinci.be', '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy',
        '0487 00 00 01', '18-09-23', 'STUDENT', '2023-2024', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Ile', 'Basile', 'Basile.Ile@student.vinci.be', '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy',
        '0488 00 00 01', '18-09-23', 'STUDENT', '2023-2024', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('skile', 'Achille', 'Achille.skile@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0490 00 00 01', '18-09-23', 'STUDENT',
        '2023-2024', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('skile', 'Carole', 'Carole.skile@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0489 00 00 01', '18-09-23', 'STUDENT',
        '2023-2024', 1);

-- Insertion de données dans la table contacts
INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (2, 9, 'ACCEPTED', 'A distance', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (4, 6, 'ACCEPTED', 'Dans l''entreprise', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_refusalReason,
                          contact_academicYear, contact_version)
VALUES (3, 6, 'TURNED_DOWN', 'A distance', 'N''ont pas accepté d''avoir un entretien', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (2, 7, 'ACCEPTED', 'Dans l''entreprise', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (2, 7, 'ON_HOLD', 'A distance', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (4, 7, 'ON_HOLD', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_refusalReason,
                          contact_academicYear, contact_version)
VALUES (3, 7, 'TURNED_DOWN', 'Dans l''entreprise', 'ne prennent qu''un seul étudiant', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (3, 5, 'ADMITTED', 'A distance', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (4, 5, 'STARTED', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (2, 5, 'STARTED', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (4, 8, 'STARTED', '2023-2024', 1);


-- Insertion des données dans la table stages
INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (9, 'Un ERP : Odoo', '2023-10-10', 1, 1, 2, 1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (6, 'sBMS project - a complex environment', '2023-11-23', 2, 2, 4, 1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (7, 'CRM : Microsoft Dynamics 365 For Sales', '2023-10-12', 4, 3, 1, 1);

-- Nombre d'utilisateurs
SELECT COUNT(user_iduser)
FROM pae.users;

-- Nombre d'entreprises
SELECT COUNT(company_idcompany)
FROM pae.companies;

-- Nombre de stages par année académique
SELECT c.contact_academicyear, COUNT(i.internship_idinternship)
FROM pae.internships i,
     pae.contacts c
WHERE i.internship_idContact = c.contact_idContact
GROUP BY c.contact_academicYear;

-- Nombre de contacts par année académique
SELECT c.contact_academicyear, COUNT(c.contact_idcontact)
FROM pae.contacts c
GROUP BY c.contact_academicyear;

-- Nombre contacts dans chacun des états
SELECT contact_state, COUNT(contact_idcontact)
FROM pae.contacts
GROUP BY contact_state;
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

INSERT INTO pae.companies (company_tradeName, company_address, company_city, company_phoneNumber, company_version,
                           company_blacklisted)
VALUES ('AXIS SRL', 'Avenue de l''Hélianthe, 63', '1180 Uccle', '02 752 17 60', 1, false);

INSERT INTO pae.companies (company_tradeName, company_address, company_city, company_phoneNumber, company_version,
                           company_blacklisted)
VALUES ('Infrabel', 'Rue Bara, 135', '1070 Bruxelles', '02 525 22 11', 1, false);

INSERT INTO pae.companies (company_tradeName, company_address, company_city, company_phoneNumber, company_version,
                           company_blacklisted)
VALUES ('La route du papier', 'Avenue des Mimosas, 83', '1150 Woluwe-Saint-Pierre', '02 586 16 65', 1, false);

INSERT INTO pae.companies (company_tradeName, company_address, company_city, company_phoneNumber, company_version,
                           company_blacklisted)
VALUES ('The Bayard Partnership', 'Grauwmeer, 1/57 bte 55', '3001 Leuven', '02 309 52 45', 1, false);

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

INSERT INTO pae.internshipSupervisors (internshipSupervisor_lastname, internshipSupervisor_firstname,
                                       internshipSupervisor_phoneNumber,
                                       internshipSupervisor_idCompany, internshipSupervisor_version)
VALUES ('Ile', 'Emile', '0489 32 16 54', 7, 1);

INSERT INTO pae.internshipSupervisors (internshipSupervisor_lastname, internshipSupervisor_firstname,
                                       internshipSupervisor_phoneNumber,
                                       internshipSupervisor_idCompany, internshipSupervisor_version)
VALUES ('Hibo', 'Owln', '0456 678 567', 6, 1);

INSERT INTO pae.internshipSupervisors (internshipSupervisor_lastname, internshipSupervisor_firstname,
                                       internshipSupervisor_phoneNumber,
                                       internshipSupervisor_idCompany, internshipSupervisor_version)
VALUES ('Barn', 'Henri', '02 752 17 60', 5, 1);


-- Insertion de données dans la table utilisateurs
INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_version)
VALUES ('Baroni', 'Raphaël', 'raphael.baroni@vinci.be', '$2a$10$Pd7afEGz1wpWEJjNVz14veBc4uSClSbOlCmDJ0FVNTG.I.GzUpZk.',
        '0481 01 01 01', '21-09-20', 'TEACHER', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_version)
VALUES ('Lehmann', 'Brigitte', 'brigitte.lehmann@vinci.be',
        '$2a$10$Pd7afEGz1wpWEJjNVz14veBc4uSClSbOlCmDJ0FVNTG.I.GzUpZk.', '0482 02 02 02', '21-09-20', 'TEACHER', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_version)
VALUES ('Leleux', 'Laurent', 'laurent.leleux@vinci.be', '$2a$10$Pd7afEGz1wpWEJjNVz14veBc4uSClSbOlCmDJ0FVNTG.I.GzUpZk.',
        '0483 03 03 03', '21-09-20', 'TEACHER', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_version)
VALUES ('Lancaster', 'Annouck', 'annouck.lancaster@vinci.be',
        '$2a$10$XaYjjV5Qk4.wJcOfB5NmfeDLN6eXt3WzMw97JMf6LD7oHcbvLzqb6', '0484 04 04 04', '21-09-20', 'ADMIN', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Line', 'Caroline', 'caroline.line@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0486 00 00 01', '18-09-23', 'STUDENT',
        '2023-2024', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Ile', 'Achille', 'ach.ile@student.vinci.be', '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy',
        '0487 00 00 01', '18-09-23', 'STUDENT', '2023-2024', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Ile', 'Basile', 'basile.ile@student.vinci.be', '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy',
        '0488 00 00 01', '18-09-23', 'STUDENT', '2023-2024', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Skile', 'Achille', 'achille.skile@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0490 00 00 01', '18-09-23', 'STUDENT',
        '2023-2024', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Skile', 'Carole', 'carole.skile@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0489 00 00 01', '18-09-23', 'STUDENT',
        '2023-2024', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Skile', 'Elle', 'elle.skile@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0491 00 00 01', '21-09-21', 'STUDENT',
        '2021-2022', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Ilotie', 'Basile', 'basile.ilotie@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0491 00 00 11', '21-09-21', 'STUDENT',
        '2021-2022', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Frilot', 'Basile', 'basile.frilot@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0491 00 00 21', '21-09-21', 'STUDENT',
        '2021-2022', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Ilot', 'Basile', 'basile.ilot@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0492 00 00 01', '21-09-21', 'STUDENT',
        '2021-2022', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Dito', 'Arnaud', 'arnaud.dito@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0493 00 00 01', '21-09-21', 'STUDENT',
        '2021-2022', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Dilo', 'Arnaud', 'arnaud.dilo@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0494 00 00 01', '21-09-21', 'STUDENT',
        '2021-2022', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Dilot', 'Cedric', 'cedric.dilot@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0495 00 00 01', '21-09-21', 'STUDENT',
        '2021-2022', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Linot', 'Auristelle', 'auristelle.linot@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0496 00 00 01', '21-09-21', 'STUDENT',
        '2021-2022', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Demoulin', 'Basile', 'basile.demoulin@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0496 00 00 02', '23-09-22', 'STUDENT',
        '2022-2023', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Moulin', 'Arthur', 'arthur.moulin@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0497 00 00 02', '23-09-22', 'STUDENT',
        '2022-2023', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Moulin', 'Hugo', 'hugo.moulin@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0497 00 00 03', '23-09-22', 'STUDENT',
        '2022-2023', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Mile', 'Aurèle', 'aurele.mile@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0497 00 00 21', '23-09-22', 'STUDENT',
        '2022-2023', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Mile', 'Frank', 'frank.mile@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0497 00 00 75', '27-09-22', 'STUDENT',
        '2022-2023', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Dumoulin', 'Basile', 'basile.dumoulin@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0497 00 00 58', '27-09-22', 'STUDENT',
        '2022-2023', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Dumoulin', 'Axel', 'axel.dumoulin@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0497 00 00 97', '27-09-22', 'STUDENT',
        '2022-2023', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Ile', 'Théophile', 'theophile.ile@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0488 35 33 89', '01-03-24', 'STUDENT',
        '2023-2024', 1);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate,
                       user_role, user_academicYear, user_version)
VALUES ('Demoulin','Jeremy','jeremy.demoulin@student.vinci.be',
        '$2a$10$4rLzRMZtodzqV.uQ0ulhaeNV.7/nW4.HpvZ3RJDoboqWc9AkAkuSy', '0497000020', '23-09-22', 'STUDENT',
        '2022-2023', 1);

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
VALUES (1, 7, 'ACCEPTED', 'Dans l''entreprise', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (2, 7, 'ON_HOLD', 'A distance', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (4, 7, 'ON_HOLD', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_refusalReason,
                          contact_academicYear, contact_version)
VALUES (3, 7, 'TURNED_DOWN', 'Dans l''entreprise', 'ne prennent qu''un seul étudiant', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_refusalreason,
                          contact_academicYear, contact_version)
VALUES (3, 5, 'TURNED_DOWN', 'A distance', 'Pas d''affinité avec ODOO', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (4, 5, 'UNSUPERVISED', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state,contact_meetplace, contact_academicYear, contact_version)
VALUES (2, 5, 'ADMITTED', 'A distance', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (4, 8, 'STARTED', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (4, 25, 'STARTED', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (3, 25, 'STARTED', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (2, 25, 'STARTED', '2023-2024', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (7, 10, 'ACCEPTED', 'A distance', '2021-2022', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (4, 13, 'UNSUPERVISED', '2021-2022', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_refusalReason,
                          contact_academicYear, contact_version)
VALUES (8, 12, 'TURNED_DOWN', 'A distance', 'ne prennent pas de stage', '2021-2022', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (4, 14, 'ACCEPTED', 'Dans l''entreprise', '2021-2022', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (4, 15, 'ACCEPTED', 'Dans l''entreprise', '2021-2022', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (1, 16, 'ACCEPTED', 'Dans l''entreprise', '2021-2022', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_refusalReason,
                          contact_academicYear, contact_version)
VALUES (4, 16, 'TURNED_DOWN', 'Dans l''entreprise', 'Choix autre étudiant', '2021-2022', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (6, 17, 'ACCEPTED', 'A distance', '2021-2022', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (4, 17, 'ON_HOLD', '2021-2022', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_refusalReason,
                          contact_academicYear, contact_version)
VALUES (3, 17, 'TURNED_DOWN', 'A distance', 'Choix autre étudiant', '2021-2022', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (1, 26, 'ACCEPTED', 'A distance', '2022-2023', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (5, 19, 'ACCEPTED', 'Dans l''entreprise', '2022-2023', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (5, 20, 'ACCEPTED', 'Dans l''entreprise', '2022-2023', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (5, 21 , 'ACCEPTED', 'A distance', '2022-2023', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (5, 22, 'ACCEPTED', 'A distance', '2022-2023', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_refusalReason,
                          contact_academicYear, contact_version)
VALUES (5, 23, 'TURNED_DOWN', 'Dans l''entreprise', 'Entretien n''a pas eu lieu', '2022-2023', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_refusalReason,
                          contact_academicYear, contact_version)
VALUES (3, 23, 'TURNED_DOWN', 'Dans l''entreprise', 'Entretien n''a pas eu lieu', '2022-2023', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_refusalReason,
                          contact_academicYear, contact_version)
VALUES (4, 23, 'TURNED_DOWN', 'Dans l''entreprise', 'Entretien n''a pas eu lieu', '2022-2023', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear,
                          contact_version)
VALUES (4, 24, 'ACCEPTED', 'A distance', '2022-2023', 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_refusalReason,
                          contact_academicYear, contact_version)
VALUES (4, 12, 'TURNED_DOWN', 'A distance', 'Choix autre étudiant', '2022-2023', 1);

-- Insertion des données dans la table stages
INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (9, 'Un ERP : Odoo', '2023-10-10', 1, 1, 2, 1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (6, 'sBMS project - a complex environment', '2023-11-23', 2, 2, 4, 1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (7, 'CRM : Microsoft Dynamics 365 For Sales', '2023-10-12', 4, 3, 1, 1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (10, 'Conservation et restauration d''oeuvres d''art', '2021-11-25',15,4,7,1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (14,'L''analyste au centre du développement', '2021-11-17',18,2,4,1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (15,'L''analyste au centre du développement', '2021-11-17',19,2,4,1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (16,'ERP : Microsoft Dynamics 366', '2021-11-23',20,3,1,1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (17,'Entretien des rails', '2023-11-22',22,5,6,1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (26,'CRM : Microsoft Dynamics 365 For Sales', '2022-11-23',25,3,1,1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (19,'Un métier : chef de projet', '2022-10-19',26,6,5,1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (20,'Un métier : chef de projet', '2022-10-19',27,6,5,1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (21,'Un métier : chef de projet', '2022-10-19',28,6,5,1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (22,'Un métier : chef de projet', '2022-10-19',29,6,5,1);

INSERT INTO pae.internships (internship_idstudent, internship_internshipproject, internship_signaturedate, internship_idcontact, internship_idinternshipsupervisor, internship_idcompany, internship_version)
VALUES (24,'sBMS project - Java Development', '2022-10-17',33,2,4,1);

-- Comptage du nombre d'utilisateurs par rôle et par année académique
SELECT u.user_role, COALESCE(u.user_academicyear, 'Non spécifié') AS user_academicyear, COUNT(u.user_iduser)
FROM pae.users u
GROUP BY u.user_role, u.user_academicyear;

--Année académique et comptage du nombre de stages par année académique
SELECT c.contact_academicyear, COUNT(i.internship_idinternship)
FROM pae.internships i,
     pae.contacts c
WHERE i.internship_idcontact = c.contact_idcontact
GROUP BY c.contact_academicyear;

--Entreprise, année académique, et comptage du nombre de stages par entreprise et par année académique
SELECT co.company_tradename, co.company_designation, c.contact_academicyear, COALESCE(COUNT(i.internship_idinternship), 0) AS nb_stages
FROM pae.contacts c
         LEFT OUTER JOIN pae.internships i ON i.internship_idcontact = c.contact_idcontact
         LEFT OUTER JOIN pae.companies co ON c.contact_idcompany = co.company_idcompany
GROUP BY co.company_tradename, co.company_designation, c.contact_academicyear
ORDER BY co.company_tradename, c.contact_academicyear;

--Année académique et comptage du nombre de contacts par année académique
SELECT c.contact_academicyear, COUNT(c.contact_idcontact)
FROM pae.contacts c
GROUP BY c.contact_academicyear;

--Etats (en format lisible pour le client) et comptage du nombre de contacts dans chacun des états
SELECT
    CASE
        WHEN c.contact_state = 'ACCEPTED' THEN 'Accepté'
        WHEN c.contact_state = 'TURNED_DOWN' THEN 'Refusé'
        WHEN c.contact_state = 'ADMITTED' THEN 'Pris'
        WHEN c.contact_state = 'STARTED' THEN 'Initié'
        WHEN c.contact_state = 'UNSUPERVISED' THEN 'Non suivi'
        WHEN c.contact_state = 'ON_HOLD' THEN 'Suspendu'
        WHEN c.contact_state = 'BLACKLISTED' THEN 'Blacklisté'
        ELSE 'Inconnu'
        END AS Etat,
    COUNT(contact_idcontact)
FROM pae.contacts c
GROUP BY contact_state;

--Année académique, états (en format lisible par le client) et comptage du nombre de contacts dans chacun des états par année académique
SELECT c.contact_academicyear,
       CASE
           WHEN c.contact_state = 'ACCEPTED' THEN 'Accepté'
           WHEN c.contact_state = 'TURNED_DOWN' THEN 'Refusé'
           WHEN c.contact_state = 'ADMITTED' THEN 'Pris'
           WHEN c.contact_state = 'STARTED' THEN 'Initié'
           WHEN c.contact_state = 'UNSUPERVISED' THEN 'Non suivi'
           WHEN c.contact_state = 'ON_HOLD' THEN 'Suspendu'
           WHEN c.contact_state = 'BLACKLISTED' THEN 'Blacklisté'
           ELSE 'Inconnu'
           END AS Etat,
       COUNT(c.contact_idcontact)
FROM pae.contacts c
GROUP BY c.contact_academicyear, c.contact_state
ORDER BY c.contact_academicyear, c.contact_state;

--Entreprise, états (en format lisible pour le client) et comptage du nombre de contacts dans chacun des états par entreprise
SELECT co.company_tradename,
       CASE
           WHEN c.contact_state = 'ACCEPTED' THEN 'Accepté'
           WHEN c.contact_state = 'TURNED_DOWN' THEN 'Refusé'
           WHEN c.contact_state = 'ADMITTED' THEN 'Pris'
           WHEN c.contact_state = 'STARTED' THEN 'Initié'
           WHEN c.contact_state = 'UNSUPERVISED' THEN 'Non suivi'
           WHEN c.contact_state = 'ON_HOLD' THEN 'Suspendu'
           WHEN c.contact_state = 'BLACKLISTED' THEN 'Blacklisté'
           ELSE 'Inconnu'
           END AS Etat,
       COUNT(c.contact_idcontact)
FROM pae.contacts c,
     pae.companies co
WHERE c.contact_idcompany = co.company_idcompany
GROUP BY co.company_tradename, c.contact_state
ORDER BY co.company_tradename, c.contact_state;
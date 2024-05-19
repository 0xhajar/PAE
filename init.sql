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
    user_academicYear TEXT NULL,
    user_version      INTEGER NOT NULL
);

CREATE TABLE pae.companies
(
    company_idCompany           SERIAL PRIMARY KEY,
    company_tradeName           TEXT    NOT NULL,
    company_designation         TEXT NULL,
    company_address             TEXT    NOT NULL,
    company_phoneNumber         TEXT NULL,
    company_email               TEXT NULL,
    company_blacklisted         BOOLEAN NOT NULL,
    company_blacklistMotivation TEXT NULL,
    company_version             INTEGER NOT NULL
);

CREATE TABLE pae.contacts
(
    contact_idContact     SERIAL PRIMARY KEY,
    contact_idCompany     INTEGER REFERENCES pae.companies (company_idcompany) NOT NULL,
    contact_idStudent     INTEGER REFERENCES pae.users (user_iduser)        NOT NULL,
    contact_state         TEXT                                         NOT NULL,
    contact_meetPlace     TEXT NULL,
    contact_refusalReason TEXT NULL,
    contact_academicYear  TEXT                                         NOT NULL,
    contact_version       INTEGER                                      NOT NULL
);

CREATE TABLE pae.internshipSupervisors
(
    internshipSupervisor_idInternshipSupervisor SERIAL PRIMARY KEY,
    internshipSupervisor_lastname               TEXT                                         NOT NULL,
    internshipSupervisor_firstname              TEXT                                         NOT NULL,
    internshipSupervisor_phoneNumber            TEXT                                         NOT NULL,
    internshipSupervisor_email                  TEXT NULL,
    internshipSupervisor_idCompany              INTEGER REFERENCES pae.companies (company_idcompany) NOT NULL,
    internshipSupervisor_version                INTEGER                                      NOT NULL
);

CREATE TABLE pae.internships
(
    internship_idInternship         SERIAL PRIMARY KEY,
    internship_idStudent            INTEGER REFERENCES pae.users (user_iduser)                                 NOT NULL,
    internship_internshipProject    TEXT NULL,
    internship_signatureDate        DATE                                                                  NOT NULL,
    internship_idContact            INTEGER REFERENCES pae.contacts (contact_idContact)                           NOT NULL,
    internship_idInternshipSupervisor INTEGER REFERENCES pae.internshipSupervisors (internshipSupervisor_idInternshipSupervisor) NOT NULL,
    internship_idCompany            INTEGER REFERENCES pae.companies (company_idcompany)                          NOT NULL,
    internship_version              INTEGER                                                               NOT NULL
);

INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phoneNumber, user_registerDate, user_role,
                       user_academicYear,
                       user_version)
VALUES ('admin', 'admin', 'admin.admin@vinci.be',
        '$2a$10$aqGDWyP8K9xeO1a8uPzcWuzf1hfiPU8IlAR8GyhgVIYhnuKNSxP7S',
        '0123456789', '2024-02-22', 'ADMIN', '2023-2024', 1);

-- mot de passe : test
INSERT INTO pae.users (user_lastname, user_firstname, user_email, user_password, user_phonenumber, user_registerdate, user_role,
                       user_academicYear,
                       user_version)
VALUES ('remmery', 'julien', 'julien.remmery@student.vinci.be',
        '$2a$10$Y3/IINNLQjpg33uhU2xhce9L5BUxyQ/ABXh35ftx4pan00lBgL8qm',
        '0123456789', '2024-01-01', 'STUDENT', '2023-2024', 1);

INSERT INTO pae.companies (company_tradeName, company_address, company_blacklisted, company_version)
VALUES ('Vinci', 'Bruxelles', false, 1);

INSERT INTO pae.companies (company_tradeName, company_address, company_blacklisted, company_version)
VALUES ('Idealis Consulting', 'Mont saint guibert', false, 1);

INSERT INTO pae.companies (company_tradeName, company_designation, company_address, company_blacklisted, company_version)
VALUES ('Sagacify', 'SRL', 'Bruxelles', false, 1);

INSERT INTO pae.companies (company_tradeName, company_designation, company_address, company_blacklisted, company_version)
VALUES ('Sagacify', 'SPRL', 'Bruxelles', false, 1);

INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (1, 2, 'STARTED', '2023-2024', 1);
INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_meetPlace, contact_academicYear, contact_version)
VALUES (2, 2, 'ADMITTED', 'dans l''entreprise', '2023-2024', 1);
INSERT INTO pae.contacts (contact_idCompany, contact_idStudent, contact_state, contact_academicYear, contact_version)
VALUES (3, 1, 'STARTED', '2023-2024', 1);

INSERT INTO pae.internshipSupervisors (internshipSupervisor_lastname, internshipSupervisor_firstname, internshipSupervisor_phoneNumber, internshipSupervisor_email, internshipSupervisor_idCompany, internshipSupervisor_version)
VALUES ('Doe', 'John', '0123456789','john.doe@sopra.be', 2, 1);
INSERT INTO pae.internships (internship_idStudent, internship_internshipProject, internship_signatureDate, internship_idContact, internship_idInternshipSupervisor, internship_idCompany, internship_version)
VALUES (2, 'Projet 1', '2024-01-01', 2, 1, 2, 1);
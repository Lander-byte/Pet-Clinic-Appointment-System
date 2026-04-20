# Pet-Clinic-Appointment-System

USE CASE DIAGRAM

<img width="577" height="546" alt="image" src="https://github.com/user-attachments/assets/59712c59-ae93-4c62-b622-28a8b06a6dad" />

CLASSES TO USE:
User - Parent Class (Abstract) 
Owner - Sub Class
Veterinarian - Sub Class
Staff - Sub Class
Pet
MedicalHistory
Species - (Enum)
Appointment
Schedule
Timeslot
TreatmentPlan
Diagnosis
Medication
Procedure
Invoice
Payment
Service

ROJ — User Domain (Abstract & Subclasses)
Classes: User (abstract), Owner, Veterinarian, Staff
This member handles the entire user hierarchy — the abstract parent and all three concrete subclasses with their specific attributes and behaviors.
STEVEN — Pet & Medical Domain
Classes: Pet, MedicalHistory, Species (enum)
This member manages everything about the animals — pet info, species categorization, and the medical history record attached to each pet.
SAIRA — Scheduling Domain
Classes: Appointment, Schedule, Timeslot
This member handles all time-related logic — how appointments are created, how a clinic's schedule is structured, and the individual timeslots within it.
BRANDON — Clinical Domain
Classes: TreatmentPlan, Diagnosis, Medication, Procedure
This member covers the clinical/medical side of a visit — what was diagnosed, what treatment is prescribed, what medications and procedures are involved.
LANHCE — Billing Domain
Classes: Invoice, Payment, Service
This member handles the financial layer — what services were rendered, how they're billed, and how payments are processed.


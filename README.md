# Purchase Request & Approval System

## Problem Statement

Create a simplified Purchase Request & Approval System:
- Staff submit purchase requests
- Multi-level managers approve/reject requests
- Finance team can view approved requests

## Business Requirements

### Staff
- Create requests
- View/update pending requests
- Submit receipts

### Approvers
- View pending requests
- Approve/reject requests
- See reviewed requests

### Finance Team
- Access and interact with requests via web UI
- Upload files

## Approval Workflow

- Requests start as **PENDING**
- Requires approval from multiple levels
- Any rejection → request becomes **REJECTED**
- All required approvals → request becomes **APPROVED**
- Approved/rejected status cannot change
- Last approver generates a Purchase Order automatically
- System must handle concurrent interactions safely

## Document Processing (AI-Based)

- **Proforma upload** → store file & extract key data
- **Automatic PO generation** → extract vendor, items, prices, terms, store PO
- **Receipt validation** → compare items/prices/seller to PO, flag discrepancies
- **Recommended libraries**: Apache PDFBox, Tess4J (Tesseract OCR), Apache POI, OpenAI API

## Technical Requirements

### Data Models

**User/Auth**
- Spring Security User or custom entity
- Roles: `STAFF`, `APPROVER_LEVEL_1`, `APPROVER_LEVEL_2`, `FINANCE`
- Role-based access control using `@PreAuthorize` annotations

**PurchaseRequest**
- title
- description
- amount
- status (PENDING/APPROVED/REJECTED)
- createdBy
- approvedBy
- timestamps (createdAt, updatedAt)
- proformaPath
- purchaseOrderPath
- receiptPath

**Optional**
- Approval entity (for tracking individual approvals)
- RequestItem entity (for line items)

### API Endpoints

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| POST | `/api/requests/` | Create request | STAFF |
| GET | `/api/requests/` | List requests (filtered by creator if STAFF) | ALL |
| GET | `/api/requests/{id}/` | View details | ALL |
| PATCH | `/api/requests/{id}/approve/` | Approve request | APPROVER |
| PATCH | `/api/requests/{id}/reject/` | Reject request | APPROVER |
| PUT | `/api/requests/{id}/` | Update request (only if PENDING) | STAFF |
| POST | `/api/requests/{id}/submit-receipt/` | Submit receipt | STAFF |

### Requirements

**Authentication**
- JWT token-based authentication using Spring Security

**Role-based Access Control**
- Using `@PreAuthorize`, `@Secured`, or method security

**Validation**
- Jakarta Bean Validation (`@Valid`, `@NotNull`, `@NotBlank`, etc.)

**HTTP Status Codes**
- Use `ResponseEntity` with appropriate `HttpStatus`

**File Uploads**
- `MultipartFile` handling with size limits

**Data Consistency**
- Use `@Transactional` with appropriate isolation levels
- Optimistic locking (`@Version`) or pessimistic locking

**Performance Optimization**
- Database indexing
- Query optimization
- Caching with Spring Cache

### Document Processing Implementation

**Proforma**
- Accept `MultipartFile` uploads
- Store to filesystem or cloud storage
- Extract metadata using PDFBox/Tess4J/OpenAI

**Automatic PO**
- Triggered on final approval
- Extract details with AI/OCR
- Generate PO document (PDF using iText or Apache POI)

**Receipt Validation**
- Accept `MultipartFile` upload
- Extract data using OCR/AI
- Compare with stored PO data
- Return `ValidationResult` DTO with discrepancies flagged

**Approach**
- Text extraction: PDFBox for text-based PDFs
- OCR: Tess4J for scanned documents
- AI/LLM: OpenAI API for intelligent extraction
- Combination based on document type

### Technology Stack

| Component | Technology |
|-----------|--------|
| Framework | Spring Boot 3.x |
| Security | Spring Security with JWT |
| Data Access | Spring Data JPA |
| Database | PostgreSQL (production), H2 (dev/test) |
| File Processing | Apache PDFBox, Tess4J, Apache POI |
| AI Integration | OpenAI Java Client |
| Build Tool | Gradle |
| API Documentation | SpringDoc OpenAPI (Swagger) |
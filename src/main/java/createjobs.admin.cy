/// <reference types="Cypress"/>

describe("Manage jobs as admin", () => {
    before(() => {
        cy.clearAllSessionStorage();
        cy.visit("http://localhost:8080");
        cy.get("#username").type(Cypress.env()["admin"].email);
        cy.get("#password").type(Cypress.env()["admin"].password);
        cy.contains("button", "Log in").click();
        cy.get("h1").should("contain", "Welcome");
    });

    it("should create 3 jobs", () => {
        // Besuche die Job-Erstellungsseite
        cy.visit("http://localhost:8080/#/jobs");

        const jobs = [
            { description: 'Job Description 1', type: 'OTHER', earnings: '1000' },
            { description: 'Job Description 2', type: 'TEST', earnings: '2000' },
            { description: 'Job Description 3', type: 'IMPLEMENT', earnings: '3000' }
        ];

        jobs.forEach(job => {
            cy.get('#description').clear().type(job.description);
            cy.get('#type').select(job.type);
            cy.get('#earnings').clear().type(job.earnings);
            cy.get('#submit').click()
        });

        // Gehe zur Job-Liste
        cy.visit("http://localhost:8080/#/jobs");

        // ÃberprÃ¼fe, ob die Tabelle 3 Elemente enthÃ¤lt
        cy.get('tbody>tr').should('have.length', 3);
    });
});
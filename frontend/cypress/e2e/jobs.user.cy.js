
/// <reference types="Cypress"/>
describe("Manage jobs as user", () => {
    before(() => {
        cy.clearAllSessionStorage();
        cy.visit("http://localhost:8080");
        cy.get("#username").type(Cypress.env()["user"].email);
        cy.get("#password").type(Cypress.env()["user"].password);
        cy.contains("button", "Log in").click();
        cy.get("h1").should("contain", "Welcome");
    });

    it("visit jobs page", () => {
        cy.get('a[href="#/jobs"]').click();
        cy.location("hash").should("include", "jobs");
    });

    it("create jobs form should not exist", () => {
        cy.get('form').should("not.exist");
    });

    it("check if first entry is first job", () => {
        cy.get('tbody>tr').should('have.length', 4);
        cy.get('tbody>tr:first-of-type').contains('td', "First Job");
    });

    it("assign first job to me", () => {
        cy.contains("tbody>tr:first-of-type>td:last-of-type>button", "Assign to me").click();
    });

    it("check if assigning worked", () => {
        cy.get("tbody>tr:first-of-type>td:last-of-type").contains("span", "Assigned")
    });
});

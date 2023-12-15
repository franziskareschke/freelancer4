/// <reference types="Cypress"/>
describe("Manage jobs as admin", () => {
    before(() => {
        cy.clearAllSessionStorage();
        cy.visit("http://localhost:8080");
        cy.get("#username").type(Cypress.env()["admin"].email);
        cy.get("#password").type(Cypress.env()["admin"].password);
        cy.contains("button", "Log in").click();
        cy.get("h1").should("contain", "Welcome");
        cy.request({
            method: "DELETE",
            url: "http://localhost:8080/api/job",
            headers: {
                Authorization: "Bearer " + sessionStorage.getItem("jwt_token"),
            },
        });
    });

    it("visit jobs page", () => {
        cy.get('a[href="#/jobs"]').click();
        cy.location("hash").should("include", "jobs");
    });

    it("create first job", () => {
        cy.get("#description").type("First Job");
        cy.get("#type").select("TEST");
        cy.get("#earnings").type("123");
        cy.contains("button", "Submit").click();
    });

    it("create second job", () => {
        cy.get("#description").clear().type("Second Job");
        cy.get("#type").select("IMPLEMENT");
        cy.get("#earnings").clear().type("50");
        cy.contains("button", "Submit").click();
    });

    it("create third job", () => {
        cy.get("#description").clear().type("Third Job");
        cy.get("#type").select("REVIEW");
        cy.get("#earnings").clear().type("69");
        cy.contains("button", "Submit").click();
    });

    it("check if three jobs were created", () => {
        cy.get('tbody>tr').should('have.length', 3)
    });

    it("create fourth job", () => {
        cy.get("#description").clear().type("Fourth Job");
        cy.get("#type").select("OTHER");
        cy.get("#earnings").clear().type("10");
        cy.contains("button", "Submit").click();
    });

    it("create fifth job", () => {
        cy.get("#description").clear().type("Fifth Job");
        cy.get("#type").select("TEST");
        cy.get("#earnings").clear().type("37");
        cy.contains("button", "Submit").click();
    });

    it("check if four jobs are loaded on first page", () => {
        cy.get('tbody>tr').should('have.length', 4)
    });

    it("visit second jobs page", () => {
        cy.get('a[href="#/jobs?page=2"]').click();
        cy.get('tbody>tr').should('have.length', 1)
        cy.get('tbody>tr:first-of-type').contains('td', "Fifth Job")
    });

    it("check if one job (fifth total) is loaded on second page", () => {
        cy.get('a[href="#/jobs?page=2"]').click();
        cy.get('tbody>tr').should('have.length', 1)
        cy.get('tbody>tr:first-of-type').contains('td', "Fifth Job")
    });

    it("assign last job to me", () => {
        cy.contains("tbody>tr:first-of-type>td:last-of-type>button", "Assign to me").click();
    });

    it("check if assigning worked", () => {
        cy.get("tbody>tr:first-of-type>td:last-of-type").contains("span", "Assigned")
    });

    it("go to first page and check all TEST jobs", () => {
        cy.get('a[href="#/jobs"]').click();
        cy.get('#filter-type').select("TEST");
        cy.contains("a", "Apply").click();
        cy.get('tbody>tr').should('have.length', 2);
    });

    it("go to first page and check all jobs earning over 69", () => {
        cy.get('#filter-type').select("");
        cy.get('#filter-earnings').type("69")
        cy.contains("a", "Apply").click();
        cy.get('tbody>tr').should('have.length', 1);
    });
});

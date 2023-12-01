describe("Job Assignment Test", () => {
    before(() => {
        cy.clearAllSessionStorage();
        cy.visit("http://localhost:8080");
        cy.get("#username").type(Cypress.env()["admin"].email);
        cy.get("#password").type(Cypress.env()["admin"].password);
        cy.contains("button", "Log in").click();
        cy.get("h1").should("contain", "Welcome");
    });

    it("should assign a job and check if it's marked as assigned", () => {
        // Gehe zur Job-Liste
        cy.visit("http://localhost:8080/#/jobs");

        // Klicke auf den "Assign to me"-Button für den ersten Job in der Liste
        cy.get('#assignToMe').click();

        // Überprüfe, ob der "Assigned"-Badge für den ersten Job in der Liste angezeigt wird
        cy.contains('.badge.bg-secondary', 'Assigned').should('be.visible');
    });
})
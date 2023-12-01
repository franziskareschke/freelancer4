describe("Job Filters Test", () => {
    describe("Manage jobs as admin", () => {
        before(() => {
            cy.clearAllSessionStorage();
            cy.visit("http://localhost:8080");
            cy.get("#username").type(Cypress.env()["admin"].email);
            cy.get("#password").type(Cypress.env()["admin"].password);
            cy.contains("button", "Log in").click();
            cy.get("h1").should("contain", "Welcome");
        });

    it("should filter jobs by type and earnings", () => {
        // Besuche die Job-Listenseite
        cy.visit("http://localhost:8080/#/jobs");

        // Wähle einen Jobtyp aus dem Dropdown
        cy.get('#filter').select('IMPLEMENT');

        // Klicke auf den „Apply“-Button
        cy.get('#apply').click();

        // Überprüfe, ob die richtige Anzahl von Jobs angezeigt wird
        // (Abhängig von der Logik deiner Anwendung)
        cy.get('tbody>tr').should('have.length', 1);

        // Gib Einnahmen im Eingabefeld ein
        cy.get("#earningsMin").type('5000');

        // Klicke erneut auf den „Apply“-Button
        cy.get('#apply').click();

        // Überprüfe erneut, ob die richtige Anzahl von Jobs angezeigt wird
        // (Abhängig von der Logik deiner Anwendung)
        cy.get('tbody>tr').should('have.length', 1);
    });
})})
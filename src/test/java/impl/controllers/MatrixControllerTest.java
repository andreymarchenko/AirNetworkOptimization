package impl.controllers;

import impl.entities.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

public class MatrixControllerTest {
    private MatrixController matrixController;
    private NetworkController networkController;
    private BusinessController businessController;

    @Before
    public void setUp() throws Exception {
        this.matrixController = new MatrixController();
        this.networkController = new NetworkController();
        this.businessController = new BusinessController();
    }

    @Test
    public void test_CalculateCost() throws Exception {
        // Arrange
        City city = Mockito.mock(City.class);
        Company company = businessController.createCompany();
        Aircraft aircraft1 = businessController.createAircraft(company, 100, 20);
        Aircraft aircraft2 = businessController.createAircraft(company, 200, 20);
        Route route1 = businessController.createRoute(city, city, aircraft1);
        Route route2 = businessController.createRoute(city, city, aircraft2);

        // Act
        double actualCost = matrixController.calculateCost(Arrays.asList(route1, route2));

        // Assert
        double expectedCost = 300;
        Assert.assertEquals(expectedCost, actualCost, 0.001);
    }

    @Test
    public void test_CalculateEstradaCoeffWithBipartivity() throws Exception {
        // Arrange
        int size = 5;
        Company company = businessController.createCompany();
        Aircraft aircraft = businessController.createAircraft(company, 100, 1);
        City moscowCity = businessController.createCity("Moscow", 0, 0);
        City londonCity = businessController.createCity("London", 200, 100);
        City berlinCity = businessController.createCity("Berlin", 100, 200);
        City parisCity = businessController.createCity("Paris", 150, 230);
        City romeCity = businessController.createCity("Rome", 200, 260);

        Network network = networkController.createNetwork(size, Arrays.asList(moscowCity, londonCity, berlinCity, parisCity, romeCity));
        networkController.addRoute(network, businessController.createRoute(moscowCity, parisCity, aircraft), true);
        networkController.addRoute(network, businessController.createRoute(moscowCity, romeCity, aircraft), true);
        networkController.addRoute(network, businessController.createRoute(londonCity, parisCity, aircraft), true);
        networkController.addRoute(network, businessController.createRoute(londonCity, romeCity, aircraft), true);
        networkController.addRoute(network, businessController.createRoute(berlinCity, parisCity, aircraft), true);
        networkController.addRoute(network, businessController.createRoute(berlinCity, romeCity, aircraft), true);

        // Act
        double actualEstradaCoeff = matrixController.calculateEstradaCoeff(network);

        // Assert
        double expectedEstradaCoeff = 1;
        Assert.assertEquals(expectedEstradaCoeff, actualEstradaCoeff, 0.001);
    }

    @Test
    public void test_CalculateEstradaCoeffWithoutBipartivity() throws Exception {
        // Arrange
        int size = 5;
        Company company = businessController.createCompany();
        Aircraft aircraft = businessController.createAircraft(company, 100, 1);
        City moscowCity = businessController.createCity("Moscow", 0, 0);
        City londonCity = businessController.createCity("London", 200, 100);
        City berlinCity = businessController.createCity("Berlin", 100, 200);
        City parisCity = businessController.createCity("Paris", 150, 230);
        City romeCity = businessController.createCity("Rome", 200, 260);

        Network network = networkController.createNetwork(size, Arrays.asList(moscowCity, londonCity, berlinCity, parisCity, romeCity));
        networkController.addRoute(network, businessController.createRoute(moscowCity, parisCity, aircraft), true);
        networkController.addRoute(network, businessController.createRoute(moscowCity, romeCity, aircraft), true);
        networkController.addRoute(network, businessController.createRoute(londonCity, parisCity, aircraft), true);
        networkController.addRoute(network, businessController.createRoute(londonCity, romeCity, aircraft), true);
        networkController.addRoute(network, businessController.createRoute(berlinCity, parisCity, aircraft), true);
        networkController.addRoute(network, businessController.createRoute(berlinCity, romeCity, aircraft), true);
        networkController.addRoute(network, businessController.createRoute(moscowCity, londonCity, aircraft), true);

        // Act
        double actualEstradaCoeff = matrixController.calculateEstradaCoeff(network);

        // Assert
        double expectedEstradaCoeff = 0.658;
        Assert.assertEquals(expectedEstradaCoeff, actualEstradaCoeff, 0.001);
    }
}

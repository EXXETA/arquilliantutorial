package com.exxeta.arquilliantutorial;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/")
@Stateless
public class RestHandler {

    @Inject
    private CarDao carDao;

    @GET
    @Path("info")
    public Response getHelloWorld() {
        return Response.ok("Hello World").build();
    }

    @GET
    @Path("car/{id}")
    public Response getCarName(@PathParam("id") int id) {

        CarEntity car = carDao.getById(id);

        Response.ResponseBuilder response;

        if (car == null) {
            response = Response.status(Response.Status.NOT_FOUND);
        } else {
            response = Response.ok(car.getName());
        }

        return response.build();
    }

}

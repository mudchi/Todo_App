/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package service;

import Entity.TASK;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author w.chirayut
 */
@Stateless
@Path("task")
public class TASKFacadeREST extends AbstractFacade<TASK> {

    @PersistenceContext(unitName = "TodoAppPU")
    private EntityManager em;

    public TASKFacadeREST() {
        super(TASK.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public Response add(TASK entity) {
        try {
            if (!"P".equals(entity.getSTATUS()) && !"D".equals(entity.getSTATUS())) {
                return Response.status(Response.Status.BAD_REQUEST).entity(
                        String.format(
                                "status %s Not Correct",
                                entity.getSTATUS())).build();
            } else if (entity.getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(
                        String.format(
                                "id is not null")).build();
            } else {
                super.add(entity);
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    String.format("%s", ex.getMessage())).build();
        }
        return Response.ok().build();
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public Response edit(@PathParam("id") Long id, TASK entity) {
        try {
            TASK _task = super.find(id);
            if (_task != null) {
                if (!"P".equals(entity.getSTATUS()) && !"D".equals(entity.getSTATUS())) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            String.format(
                                    "status %s Not Correct",
                                    entity.getSTATUS())).build();
                } else if (entity.getId() == null) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            String.format(
                                    "id is not null")).build();
                } else {
                    super.edit(entity);
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(
                        String.format(
                                "id %s Not Found",
                                id)).build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    String.format("%s", ex.getMessage())).build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        TASK _task = super.find(id);
        if (_task != null) {
            super.delete(super.find(id));
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(
                    String.format(
                            "id %s Not Found",
                            id)).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public TASK view(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<TASK> viewAll() {
        return super.viewAll();
    }

    @PUT
    @Path("set/{id}/{status}")
    @Produces({"application/xml", "application/json"})
    public Response setTask(@PathParam("id") Long id, @PathParam("status") String status) {
        if ("P".equals(status) || "D".equals(status)) {
            TASK _task = super.find(id);
            if (_task != null) {
                _task.setSTATUS(status);
                super.edit(_task);
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(
                        String.format(
                                "id %s Not Found",
                                id)).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    String.format(
                            "status %s Not Correct",
                            status)).build();
        }
        return Response.ok().build();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}

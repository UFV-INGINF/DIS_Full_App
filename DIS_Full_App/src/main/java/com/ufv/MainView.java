package com.ufv;

import com.google.gson.Gson;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * The main view contains a button and a click listener.
 */
@Route
@PWA(name = "My Application", shortName = "My Application")
public class MainView extends HorizontalLayout {

    private static final String api = "https://swapi.dev/api/%s/%s";
    HttpRequest request;
    HttpClient client = HttpClient.newBuilder().build();
    HttpResponse<String> response;


    private String getCharacter(String type, String id) {
        try {
            String resource = String.format(api, type, id);
            System.out.println(resource);
            request = HttpRequest
                    .newBuilder(new URI(resource))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return response.body();

    }


    public MainView() {

        final Gson gson = new Gson();

        HorizontalLayout mainLeft = new HorizontalLayout();
        HorizontalLayout mainRight = new HorizontalLayout();

        mainRight.setPadding(true);

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("people", "planets", "starships");
        valueSelect.setPlaceholder("Elija un tipo de recurso");

        TextField placeholderField = new TextField();
        placeholderField.setPlaceholder("Introduzca el id del recurso");

        horizontalLayout.add(valueSelect, placeholderField);


        Button button = new Button("Obtener",
                event -> {
                    String resource = valueSelect.getValue();
                    String id = placeholderField.getValue();
                    String response = getCharacter(resource, id);

                    Character character = gson.fromJson(response, Character.class);

                    mainRight.add(character.getName());
                });
        mainLeft.add(horizontalLayout, button);
        add(mainLeft, mainRight);

    }
}

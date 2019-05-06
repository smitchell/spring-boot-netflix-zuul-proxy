# Proxy Example

This is a Zuul Proxy example used to set-up integration tests. Currently, I´m testing Spring Security.

### PROBLEM STATEMENT

For this integration test, create a PCF Space that includes a service registry and these micro-services:
* **Angular Example** - If you bypass the proxy and go to the angular example route the page loads. [TRY IT](https://angular-example.test.medzero.cfapps.io/).
* **Authentication Example** - If you bypass the proxy and go to the authentication server root you can sign in and access a protected page. [TRY IT](https://auth-example.test.medzero.cfapps.io/)
* **Zuul Proxy Example** - If you try to access the angular example via the proxy you do not get the expected result. [TRY IT](https://zuul-proxy-example.test.medzero.cfapps.io/angular-example/)

#### Expected Result
1) User tries to access the Angular site via an SSO-enabled proxy server.
2) Spring Security on the proxy server redirects the user´s browser to the Login page on the authentication server.
3) The user signs in and posts the Login form to the authentication server.
4) The authentication server creates a JWT oauth2 token and forwards the user to the orginally requested Angular site.

#### Actual Results
The authentication server forwards the user back to the Login page.

### How do I run this?

Since we are using a Service Registry you must set the route in the manifest.yml file of all three projects, plus you must 
set some URLs in the application.yml files of the proxy server and authentication server.

#### Create a PCF Test Space for the Demo
1) Create a new PCF test Space.
2) Add a Service Registry.
3) `cf login` or `cf t -s [space name]` to point to the new test space.

#### Proxy Server
1) Edit manifest.yml and change the route to one that works in your test PCS Space.
2) Edit /src/main/resources/application.yml and change the following properties to match your test Space routes:
* example:proxy:logout-url
* zuul:routes:auth-service:url
* zuul:routes:angular-example:url
* security:oauth2:client:accessTokenUri
* security:oauth2:client:userAuthorizationUri
* eureka:client:service-url:defaultZone

#### Angular Example

1) Clone the Angular Example from here: [https://github.com/smitchell/cloud-foundry-angular-example.git]
2) Edit manifest.yml and change the route to one that works in your test PCF Space.
3) Run `ng build --prod`. 
4) `cf push` to deploy to your test space.
5) Navigate to the Server Registry and bind the Angular Example app. You will have to restage the app after binding.

#### Spring Security 5 Upgrade SSO Auth Server

The authentication server automatically generates the list of registered redirect URIs for this example based on two URLs properties
in /src/main/resources/application.yml: example:auth-url and example:proxy-url.

1) Clone the authentication server from here: [https://github.com/smitchell/spring-security-5-upgrade_sso-auth-server.git]
2) Edit manifest.yml and change the route to one that works in your test PCF Space.
3) Edit /src/main/resources/application.yml and set the example:auth-url and example:proxy-url properties for your test Space.

### Test Your Setup
1) Verify the Angular app is running by hitting its assigned route (i.e. [https://angular-example.test.medzero.cfapps.io]). Note, if the app is bound to the service register the context, /angular-example, will get appended to the URL when the page loads.
2) Verify that the Authentication Server is running by hitting its assign route. Sign in as steve/password.
3) Verify the proxy is running by hitting its assigned route + /angular-example.

It **should** load the angular page when you sign in, but as of this writing you get the /login page again instead.



# OSGi - Swagger Connector 1.0

## Features
The OSGi - Swagger connector provides one simple OSGi bundle, which exposes Swagger documentation (computed from JAX-RS services exposed via the [OSGi - JAX-RS Connector](https://github.com/hstaudacher/osgi-jax-rs-connector/)) in OSGi environments.
It contains a wrapper bundle for json4s, so it can work in OSGi environments.

## Notes
 * Tested it only on Equinox based OSGi environments running mostly from Eclipse (but it might work in Virgo as well).

## Installation
As soon as I finish the Tycho build for the project I'll provide a p2 update site with all required bundles. Until then you have to compile, package and deploy the bundles. See next section.

## Run/Try it out
For now you have to compile for yourself. To do this you have to setup the target platform, which is located under the releng project.
Unfortunately Swagger is not so friendly with OSGi, I had to replace/add proper manifest content in order to make it work properly in OSGi envs.
To use this customized Swagger, you have to execute mvn clean verify jetty:run -Pregenerate in the dep.p2 project. 
This will generate a local p2 update site with the proper Swagger bundles.
After this head over to swagger.target project and open the com.cmark.swagger.target.target file and click on Set as Target platform (assuming you are working in Eclipse).
After that head over to the example project and use the provided launch config to start an OSGi environment with the bundles.

## Requirements
 * JRE 1.7
 * [OSGi - JAX-RS Connector](https://github.com/hstaudacher/osgi-jax-rs-connector/)
 * OSGi Core Specification R4 and an OSGi HttpService implementation (e.g. Equinox, Felix).
 * The swagger.ui bundle requires Equinox based OSGi environment.

## Contributions
Feel free to contribute if you'd like so.
Fork this repository, commit your changes and send a pull request.

## License

[Eclipse Public License 1.0](https://www.eclipse.org/legal/epl-v10.html)


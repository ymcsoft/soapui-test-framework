# soapui-test-framework

Automation testing library using SoapUI projects

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You need to have jdk 8 or later and maven 3.x or later installed to be able to build the project

### Installing

Clone from GitHub repository

```
git clone https://github.com/ymcsoft/soapui-test-framework.git
```

And build

```
cd soapui-test-framework
mvn package
```
It will produce **soapui-test-framework-{version}.jar** artifact in the *target* directory

## Running the tests

Use the following maven command to run the tests

```
mvn tests
```

## Deployment

This command will install soapui-test-framework artifact to your local maven repository

```
mvn install
```
if you installing release version use _release_ profile to add javadoc, sources and gpg signatures:

```
mvn install -P release
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management System

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/ymcsoft/function-utils/tags). 

## Authors

* **Yuri Moiseyenko** - *Initial work*

See also the list of [contributors](https://github.com/ymcsoft/contributors) who participated in this project.

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

* Hat tip to **Volodymyr Shchelkanov** for his help and advice how to integrate SoapUI with Java
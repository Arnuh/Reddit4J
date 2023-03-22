![Discord](https://img.shields.io/discord/1088011754115706952)

# Adding Reddit4J as a dependency

Reddit4J is still in early development, and you should expect breaking changes in minor updates.

## Maven

```xml

<repositories>
	<repository>
		<id>arnah-group</id>
		<url>https://repo.arnah.ca/repository/maven-group/</url>
	</repository>
</repositories>
```

```xml

<dependency>
	<groupId>ca.arnah</groupId>
	<artifactId>reddit4j</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Gradle

```kotlin
allprojects {
	repositories {
		maven { url 'https://repo.arnah.ca/repository/maven-group/' }
	}
}
```

``` kotlin
dependencies {
        implementation 'ca.arnah:reddit4j:0.0.1-SNAPSHOT'
}
```

# Contributing

We are always looking for people to help around and contribute their ideas, as maintaining such a large project is far from a one-man project.
There are multiple ways to contribute, including:

### New features

If there's a feature which you need, however it is not present in the wrapper right now, and you believe it could be useful for other people, it might be worth
building it directly into the wrapper and contributing it into the main project. The steps for doing so are:

1. Fork the project
2. Make the wanted changes on a new branch
3. Submit a pull request
4. Discuss with reviewer, and implement changes if needed
5. Branch gets merged back into the project

A list of features which will generally get accepted really easily include, but are not limited to:

- Extra coverage for not covered yet endpoints, from the main docs
- Fixing issues, or making some request more efficient
- Converting some String value in a Reddit object into an Enum (be aware that sufficient observation must also be done)
- Adding new values to an enum (however they must be accompanied by a reproducible request which shows the value happening)

#### A quick note on undocumented endpoints

We ❤️ undocumented endpoints. We think it is what sets apart a simple robotic wrapper, from one that truly empowers developers to do wonderful things. However,
when a request for one comes in, please keep in mind that since it is undocumented, it might take a little longer to get it implemented since it will take a bit
to explore it and the full consequences it brings.

### Reporting bugs

If you have found a bug, [open an issue](https://github.com/Arnuh/Reddit4J/issues), and someone will approve it.

### Fixing issues

If you see an issue which you believe you can fix, follow the steps outlined for the new features, and submit a pull request. Ideally, mention the issue you
want to fix (if there's one open) in your pull request.

### Credits

Original [Reddit4J](https://github.com/masecla22/Reddit4J) from masecla22

Original refactor taken from a PR made by [yvasyliev](https://github.com/masecla22/Reddit4J/pull/11)
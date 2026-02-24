# Publishing to Maven Central (Sonatype)

This project is set up to publish to [Maven Central](https://central.sonatype.com/) via [Sonatype](https://central.sonatype.org/) using the **Central Publisher Portal** and the OSSRH Staging API compatibility service.

## Prerequisites

1. **Sonatype / Central account**  
   - Sign up at [central.sonatype.com](https://central.sonatype.com/) and claim the namespace that matches your `groupId` (e.g. `com.kassa`).  
   - See [Publish to Maven Central](https://central.sonatype.org/publish/).

2. **Portal User Token**  
   - Generate a token at [Generate Portal Token](https://central.sonatype.com/publishing/namespaces).  
   - Use the token **username** and **password** in Maven `settings.xml` (see below).

3. **GPG key** (for signing artifacts)  
   - Create a key: `gpg --gen-key`.  
   - Publish the public key: `gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID`.  
   - You will use this key when running the release (e.g. `-Dgpg.keyname=YOUR_KEY_ID` or let GPG use the default).

## Maven `settings.xml`

Configure the Sonatype server with your **Portal User Token** (not the old OSSRH token):

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>YOUR_PORTAL_TOKEN_USERNAME</username>
      <password>YOUR_PORTAL_TOKEN_PASSWORD</password>
    </server>
  </servers>
</settings>
```

Place this in `~/.m2/settings.xml` (or your CI secrets). The `id` must be `ossrh` to match `pom.xml`’s `distributionManagement` and `nexus-staging-maven-plugin`.

## Repository URLs

- **Snapshots** are deployed to:  
  `https://central.sonatype.com/repository/maven-snapshots/`
- **Releases** are deployed to the OSSRH Staging API; after closing and releasing in the Portal, they appear on Maven Central.

See [Publish via OSSRH Staging API](https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/).

## Releasing a version

### 1. Prepare the release

- Set the release version in `pom.xml` (e.g. `0.0.1`; remove `-SNAPSHOT`).
- Commit and tag, e.g. `v0.0.1`.

### 2. Build, sign, and deploy

From the project root:

```bash
./mvnw clean deploy -DskipTests -Dgpg.skip=false
```

- Signing uses your default GPG key. To use a specific key:  
  `-Dgpg.keyname=YOUR_KEY_ID`
- For headless/CI, ensure GPG can sign (e.g. passphrase via env or agent). The POM configures `--pinentry-mode loopback` for non-interactive use.

This will:

- Build the project and attach sources and javadoc JARs.
- Sign all artifacts with GPG.
- Deploy to the OSSRH Staging API (creates a staging repository).

### 3. Close and release in Central Portal

- Open [Central Publisher Portal](https://central.sonatype.com/publishing).
- Find the new staging repository (from the deploy step).
- **Close** the repository (validation runs).
- After validation passes, **Release** it to Maven Central.

Alternatively, you can use the Nexus Staging Maven Plugin from the command line to close/release (see plugin docs and Portal API if you prefer automation).

### 4. After release

- Bump the version in `pom.xml` back to the next SNAPSHOT (e.g. `0.0.2-SNAPSHOT`) and commit.
- Update **README.md** (and any docs) to reference the new version instead of `0.0.1-SNAPSHOT`.

## Publishing snapshots

Snapshot deployments do not require GPG signing. Deploy with:

```bash
./mvnw clean deploy -DskipTests -Dgpg.skip=true
```

Snapshots go to the configured snapshot repository (Central’s maven-snapshots).

## Checklist before first release

- [ ] Namespace (e.g. `com.kassa`) claimed in Central Publisher Portal.
- [ ] Portal User Token created and stored in `settings.xml` under `<server id="ossrh">`.
- [ ] GPG key created and public key published to a keyserver.
- [ ] `pom.xml`: `url`, `scm`, `developers`, `description`, and `license` are correct.
- [ ] Replace placeholder SCM/URL in `pom.xml` if your repo is not `https://github.com/kassa/sep-spring-boot-starter`.

## References

- [Publish to Maven Central](https://central.sonatype.org/publish/)
- [Publish via OSSRH Staging API](https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/)
- [Generate Portal Token](https://central.sonatype.com/publishing/namespaces)
- [Nexus Staging Maven Plugin](https://github.com/sonatype/nexus-maven-plugins/tree/main/staging/maven-plugin)

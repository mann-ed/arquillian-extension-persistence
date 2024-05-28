package org.arquillian.ape.rdbms.flyway;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.arquillian.ape.core.RunnerExpressionParser;
import org.flywaydb.core.api.callback.FlywayCallback;
import org.flywaydb.core.api.resolver.MigrationResolver;

public class FlywayOptions implements Map<String, Object> {

    static final String INSTALLED_BY                    = "installedBy";
    static final String ALLOW_MIXED_MIGRATIONS          = "allowMixedMigrations";
    static final String IGNORE_MISSING_MIGRATIONS       = "ignoreMissingMigrations";
    static final String IGNORE_FUTURE_MIGRATIONS        = "ignoreFutureMigrations";
    static final String IGNORE_FAILED_FUTURE_MIGRATIONS = "ignoreFailedFutureMigrations";
    static final String VALIDATE_ON_MIGRATE             = "validateOnMigrate";
    static final String CLEAN_ON_VALIDATION_ERROR       = "cleanOnValidationError";
    static final String ENCODING                        = "encoding";
    static final String SCHEMAS                         = "schemas";
    static final String TABLE                           = "table";
    static final String TARGET                          = "target";
    static final String PLACEHOLDER_REPLACEMENT         = "placeholderReplacement";
    static final String PLACEHOLDERS                    = "placeholders";
    static final String PLACEHOLDER_SUFFIX              = "placeholderSuffix";
    static final String PLACEHOLDER_PREFIX              = "placeholderPrefix";
    static final String SQL_MIGRATION_PREFIX            = "sqlMigrationPrefix";
    static final String REPEATABLE_SQL_MIGRATION_PREFIX = "repeatableSqlMigrationPrefix";
    static final String SQL_MIGRATION_SEPARATOR         = "sqlMigrationSeparator";
    static final String SQL_MIGRATION_SUFFIX            = "sqlMigrationSuffix";
    static final String BASELINE_VERSION                = "baselineVersion";
    static final String BASELINE_DESCRIPTION            = "baselineDescription";
    static final String BASELINE_ON_MIGRATE             = "baselineOnMigrate";
    static final String OUT_OF_ORDER                    = "outOfOrder";
    static final String CALLBACKS                       = "callbacks";
    static final String CALLBACKS_STRING                = "callbacksString";
    static final String SKIP_DEFAULT_CALLBACK           = "skipDefaultCallback";
    static final String RESOLVERS                       = "resolvers";
    static final String RESOLVERS_STRING                = "resolversString";
    static final String SKIP_DEFAULT_RESOLVERS          = "skipDefaultResolvers";

    private Map<String, Object> options = new HashMap<>();

    private FlywayOptions() {
    }

    FlywayOptions(final Map<String, Object> options) {
        this.options.putAll(options);
    }

    public static FlywayConfigurationOptions options() {
        return new FlywayConfigurationOptions();
    }

    @Override
    public int size() {
        return options.size();
    }

    @Override
    public boolean isEmpty() {
        return options.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return options.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return options.containsValue(value);
    }

    @Override
    public Object get(final Object key) {
        return options.get(key);
    }

    @Override
    public Object put(final String key, final Object value) {
        return options.put(key, value);
    }

    @Override
    public Object remove(final Object key) {
        return options.remove(key);
    }

    @Override
    public void putAll(final Map<? extends String, ?> m) {
        options.putAll(m);
    }

    @Override
    public void clear() {
        options.clear();
    }

    @Override
    public Set<String> keySet() {
        return options.keySet();
    }

    @Override
    public Collection<Object> values() {
        return options.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return options.entrySet();
    }

    void configure(final org.flywaydb.core.Flyway flyway) {
        if (this.options.containsKey(INSTALLED_BY)) {
            flyway.setInstalledBy((String) this.options.get(INSTALLED_BY));
        }

        if (this.options.containsKey(ALLOW_MIXED_MIGRATIONS)) {
            flyway.setAllowMixedMigrations((Boolean) this.options.get(ALLOW_MIXED_MIGRATIONS));
        }

        if (this.options.containsKey(IGNORE_MISSING_MIGRATIONS)) {
            flyway.setIgnoreMissingMigrations((Boolean) this.options.get(IGNORE_MISSING_MIGRATIONS));
        }

        if (this.options.containsKey(IGNORE_FUTURE_MIGRATIONS)) {
            flyway.setIgnoreFutureMigrations((Boolean) this.options.get(IGNORE_FUTURE_MIGRATIONS));
        }

        if (this.options.containsKey(IGNORE_FAILED_FUTURE_MIGRATIONS)) {
            flyway.setIgnoreFailedFutureMigration((Boolean) this.options.get(IGNORE_FAILED_FUTURE_MIGRATIONS));
        }

        if (this.options.containsKey(VALIDATE_ON_MIGRATE)) {
            flyway.setValidateOnMigrate((Boolean) this.options.get(VALIDATE_ON_MIGRATE));
        }

        if (this.options.containsKey(CLEAN_ON_VALIDATION_ERROR)) {
            flyway.setCleanOnValidationError((Boolean) this.options.get(CLEAN_ON_VALIDATION_ERROR));
        }

        if (this.options.containsKey(ENCODING)) {
            flyway.setEncoding((String) this.options.get(ENCODING));
        }

        if (this.options.containsKey(SCHEMAS)) {
            flyway.setSchemas((String[]) this.options.get(SCHEMAS));
        }

        if (this.options.containsKey(TABLE)) {
            flyway.setTable((String) this.options.get(TABLE));
        }

        if (this.options.containsKey(TARGET)) {
            flyway.setTargetAsString((String) this.options.get(TARGET));
        }

        if (this.options.containsKey(PLACEHOLDER_REPLACEMENT)) {
            flyway.setPlaceholderReplacement((Boolean) this.options.get(PLACEHOLDER_REPLACEMENT));
        }

        if (this.options.containsKey(PLACEHOLDERS)) {
            flyway.setPlaceholders((Map<String, String>) this.options.get(PLACEHOLDERS));
        }

        if (this.options.containsKey(PLACEHOLDER_SUFFIX)) {
            flyway.setPlaceholderSuffix((String) this.options.get(PLACEHOLDER_SUFFIX));
        }

        if (this.options.containsKey(PLACEHOLDER_PREFIX)) {
            flyway.setPlaceholderPrefix((String) this.options.get(PLACEHOLDER_PREFIX));
        }

        if (this.options.containsKey(SQL_MIGRATION_PREFIX)) {
            flyway.setSqlMigrationPrefix((String) this.options.get(SQL_MIGRATION_PREFIX));
        }

        if (this.options.containsKey(REPEATABLE_SQL_MIGRATION_PREFIX)) {
            flyway.setRepeatableSqlMigrationPrefix((String) this.options.get(REPEATABLE_SQL_MIGRATION_PREFIX));
        }

        if (this.options.containsKey(SQL_MIGRATION_SEPARATOR)) {
            flyway.setSqlMigrationSeparator((String) this.options.get(SQL_MIGRATION_SEPARATOR));
        }

        if (this.options.containsKey(BASELINE_VERSION)) {
            flyway.setBaselineVersionAsString((String) this.options.get(BASELINE_VERSION));
        }

        if (this.options.containsKey(BASELINE_DESCRIPTION)) {
            flyway.setBaselineDescription((String) this.options.get(BASELINE_DESCRIPTION));
        }

        if (this.options.containsKey(SQL_MIGRATION_SUFFIX)) {
            flyway.setSqlMigrationSuffix((String) this.options.get(SQL_MIGRATION_SUFFIX));
        }

        if (this.options.containsKey(BASELINE_ON_MIGRATE)) {
            flyway.setBaselineOnMigrate((Boolean) this.options.get(BASELINE_ON_MIGRATE));
        }

        if (this.options.containsKey(OUT_OF_ORDER)) {
            flyway.setOutOfOrder((Boolean) this.options.get(OUT_OF_ORDER));
        }

        if (this.options.containsKey(SKIP_DEFAULT_CALLBACK)) {
            flyway.setSkipDefaultCallbacks((Boolean) this.options.get(SKIP_DEFAULT_CALLBACK));
        }

        if (this.options.containsKey(SKIP_DEFAULT_RESOLVERS)) {
            flyway.setSkipDefaultResolvers((Boolean) this.options.get(SKIP_DEFAULT_RESOLVERS));
        }

        if (this.options.containsKey(CALLBACKS)) {
            flyway.setCallbacks((FlywayCallback[]) this.options.get(CALLBACKS));
        }

        if (this.options.containsKey(CALLBACKS_STRING)) {
            flyway.setCallbacksAsClassNames((String[]) this.options.get(CALLBACKS_STRING));
        }

        if (this.options.containsKey(RESOLVERS)) {
            flyway.setResolvers((MigrationResolver[]) this.options.get(RESOLVERS));
        }

        if (this.options.containsKey(RESOLVERS_STRING)) {
            flyway.setResolversAsClassNames((String[]) this.options.get(RESOLVERS_STRING));
        }
    }

    public static FlywayOptions from(final FlywayConfiguration flywayConfiguration) {
        final Map<String, Object> options = new HashMap<>();

        if (!flywayConfiguration.installedBy().isEmpty()) {
            options.put(INSTALLED_BY, RunnerExpressionParser.parseExpressions(flywayConfiguration.installedBy()));
        }

        if (!flywayConfiguration.allowMixedMigrations().isEmpty()) {
            options.put(ALLOW_MIXED_MIGRATIONS, Boolean.parseBoolean(
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.allowMixedMigrations())));
        }

        if (!flywayConfiguration.ignoreMissingMigrations().isEmpty()) {
            options.put(IGNORE_MISSING_MIGRATIONS, Boolean.parseBoolean(
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.ignoreMissingMigrations())));
        }

        if (!flywayConfiguration.ignoreFutureMigrations().isEmpty()) {
            options.put(IGNORE_FUTURE_MIGRATIONS, Boolean.parseBoolean(
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.ignoreFutureMigrations())));
        }

        if (!flywayConfiguration.ignoreFailedFutureMigrations().isEmpty()) {
            options.put(IGNORE_FAILED_FUTURE_MIGRATIONS, Boolean.parseBoolean(
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.ignoreFailedFutureMigrations())));
        }

        if (!flywayConfiguration.validateOnMigrate().isEmpty()) {
            options.put(VALIDATE_ON_MIGRATE, Boolean.parseBoolean(
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.validateOnMigrate())));
        }

        if (!flywayConfiguration.cleanOnValidationError().isEmpty()) {
            options.put(CLEAN_ON_VALIDATION_ERROR, Boolean.parseBoolean(
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.cleanOnValidationError())));
        }

        if (!flywayConfiguration.encoding().isEmpty()) {
            options.put(ENCODING, RunnerExpressionParser.parseExpressions(flywayConfiguration.encoding()));
        }

        if (flywayConfiguration.schemas().length > 0) {
            final List<String> resolved = Arrays.stream(flywayConfiguration.schemas())
                    .map(RunnerExpressionParser::parseExpressions)
                    .collect(Collectors.toList());

            options.put(SCHEMAS, resolved.toArray(new String[resolved.size()]));
        }

        if (!flywayConfiguration.table().isEmpty()) {
            options.put(TABLE, RunnerExpressionParser.parseExpressions(flywayConfiguration.table()));
        }

        if (!flywayConfiguration.target().isEmpty()) {
            options.put(TARGET, RunnerExpressionParser.parseExpressions(flywayConfiguration.target()));
        }

        if (flywayConfiguration.placeholders().length > 0) {
            final Map<String, String> placeholders = Arrays.stream(flywayConfiguration.placeholders())
                    .collect(Collectors.toMap(p -> p.key(), p -> RunnerExpressionParser.parseExpressions(p.value())));

            options.put(PLACEHOLDERS, placeholders);
        }

        if (!flywayConfiguration.placeholderReplacement().isEmpty()) {
            options.put(PLACEHOLDER_REPLACEMENT, Boolean.parseBoolean(
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.placeholderReplacement())));
        }

        if (!flywayConfiguration.placeholderSuffix().isEmpty()) {
            options.put(PLACEHOLDER_SUFFIX,
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.placeholderSuffix()));
        }

        if (!flywayConfiguration.placeholderPrefix().isEmpty()) {
            options.put(PLACEHOLDER_PREFIX,
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.placeholderPrefix()));
        }

        if (!flywayConfiguration.sqlMigrationPrefix().isEmpty()) {
            options.put(SQL_MIGRATION_PREFIX,
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.sqlMigrationPrefix()));
        }

        if (!flywayConfiguration.repeatableSqlMigrationPrefix().isEmpty()) {
            options.put(REPEATABLE_SQL_MIGRATION_PREFIX,
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.repeatableSqlMigrationPrefix()));
        }

        if (!flywayConfiguration.sqlMigrationSeparator().isEmpty()) {
            options.put(SQL_MIGRATION_SEPARATOR,
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.sqlMigrationSeparator()));
        }

        if (!flywayConfiguration.sqlMigrationSuffix().isEmpty()) {
            options.put(SQL_MIGRATION_SUFFIX,
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.sqlMigrationSuffix()));
        }

        if (!flywayConfiguration.baselineVersion().isEmpty()) {
            options.put(BASELINE_VERSION,
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.baselineVersion()));
        }

        if (!flywayConfiguration.baselineDescription().isEmpty()) {
            options.put(BASELINE_DESCRIPTION,
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.baselineDescription()));
        }

        if (!flywayConfiguration.baselineOnMigrate().isEmpty()) {
            options.put(BASELINE_ON_MIGRATE,
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.baselineOnMigrate()));
        }

        if (!flywayConfiguration.outOfOrder().isEmpty()) {
            options.put(OUT_OF_ORDER, Boolean.parseBoolean(
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.outOfOrder())));
        }

        if (flywayConfiguration.callbacks().length > 0) {
            final List<String> callbacks = Arrays.stream(flywayConfiguration.callbacks())
                    .map(RunnerExpressionParser::parseExpressions)
                    .collect(Collectors.toList());

            options.put(CALLBACKS_STRING, callbacks.toArray(new String[callbacks.size()]));
        }

        if (!flywayConfiguration.skipDefaultCallback().isEmpty()) {
            options.put(SKIP_DEFAULT_CALLBACK, Boolean.parseBoolean(
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.skipDefaultCallback())));
        }

        if (flywayConfiguration.resolvers().length > 0) {
            final List<String> resolvers = Arrays.stream(flywayConfiguration.resolvers())
                    .map(RunnerExpressionParser::parseExpressions)
                    .collect(Collectors.toList());

            options.put(RESOLVERS_STRING, resolvers.toArray(new String[resolvers.size()]));
        }

        if (!flywayConfiguration.skipDefaultResolvers().isEmpty()) {
            options.put(SKIP_DEFAULT_RESOLVERS, Boolean.parseBoolean(
                    RunnerExpressionParser.parseExpressions(flywayConfiguration.skipDefaultResolvers())));
        }

        return new FlywayOptions(options);
    }

    public static class FlywayConfigurationOptions {
        private FlywayOptions flywayOptions = new FlywayOptions();

        private FlywayConfigurationOptions() {
        }

        public FlywayConfigurationOptions installedBy(final String installedBy) {
            flywayOptions.put(FlywayOptions.INSTALLED_BY, installedBy);
            return this;
        }

        public FlywayConfigurationOptions allowMixedMigrations(final Boolean allowMixedMigrations) {
            flywayOptions.put(FlywayOptions.ALLOW_MIXED_MIGRATIONS, allowMixedMigrations);
            return this;
        }

        public FlywayConfigurationOptions ignoreMissingMigrations(final Boolean ignoreMissingMigrations) {
            flywayOptions.put(FlywayOptions.IGNORE_MISSING_MIGRATIONS, ignoreMissingMigrations);
            return this;
        }

        public FlywayConfigurationOptions ignoreFutureMigrations(final Boolean ignoreFutureMigrations) {
            flywayOptions.put(FlywayOptions.IGNORE_FUTURE_MIGRATIONS, ignoreFutureMigrations);
            return this;
        }

        public FlywayConfigurationOptions ignoreFailedFutureMigrations(final Boolean ignoreFailedFutureMigrations) {
            flywayOptions.put(FlywayOptions.IGNORE_FAILED_FUTURE_MIGRATIONS, ignoreFailedFutureMigrations);
            return this;
        }

        public FlywayConfigurationOptions validateOnMigrate(final Boolean validateOnMigrate) {
            flywayOptions.put(FlywayOptions.VALIDATE_ON_MIGRATE, validateOnMigrate);
            return this;
        }

        public FlywayConfigurationOptions cleanOnValidationError(final Boolean cleanOnValidationError) {
            flywayOptions.put(FlywayOptions.CLEAN_ON_VALIDATION_ERROR, cleanOnValidationError);
            return this;
        }

        public FlywayConfigurationOptions encoding(final String encoding) {
            flywayOptions.put(FlywayOptions.ENCODING, encoding);
            return this;
        }

        public FlywayConfigurationOptions schemas(final String... schemas) {
            flywayOptions.put(FlywayOptions.SCHEMAS, schemas);
            return this;
        }

        public FlywayConfigurationOptions table(final String table) {
            flywayOptions.put(FlywayOptions.TABLE, table);
            return this;
        }

        public FlywayConfigurationOptions target(final String target) {
            flywayOptions.put(FlywayOptions.TARGET, target);
            return this;
        }

        public FlywayConfigurationOptions placeholderReplacement(final Boolean placeholderReplacement) {
            flywayOptions.put(FlywayOptions.PLACEHOLDER_REPLACEMENT, placeholderReplacement);
            return this;
        }

        public FlywayConfigurationOptions placeholders(final Map<String, String> placeholders) {
            flywayOptions.put(FlywayOptions.PLACEHOLDERS, placeholders);
            return this;
        }

        public FlywayConfigurationOptions placeholderSuffix(final String placeholderSuffix) {
            flywayOptions.put(FlywayOptions.PLACEHOLDER_SUFFIX, placeholderSuffix);
            return this;
        }

        public FlywayConfigurationOptions placeholderPrefix(final String placeholderPrefix) {
            flywayOptions.put(FlywayOptions.PLACEHOLDER_PREFIX, placeholderPrefix);
            return this;
        }

        public FlywayConfigurationOptions sqlMigrationPrefix(final String sqlMigrationPrefix) {
            flywayOptions.put(FlywayOptions.SQL_MIGRATION_PREFIX, sqlMigrationPrefix);
            return this;
        }

        public FlywayConfigurationOptions repeatableSqlMigrationPrefix(final String repeatableSqlMigrationPrefix) {
            flywayOptions.put(FlywayOptions.REPEATABLE_SQL_MIGRATION_PREFIX, repeatableSqlMigrationPrefix);
            return this;
        }

        public FlywayConfigurationOptions sqlMigrationSeparator(final String sqlMigrationSeparator) {
            flywayOptions.put(FlywayOptions.SQL_MIGRATION_SEPARATOR, sqlMigrationSeparator);
            return this;
        }

        public FlywayConfigurationOptions baselineVersion(final String baselineVersion) {
            flywayOptions.put(FlywayOptions.BASELINE_VERSION, baselineVersion);
            return this;
        }

        public FlywayConfigurationOptions baselineDescription(final String baselineDescription) {
            flywayOptions.put(FlywayOptions.BASELINE_DESCRIPTION, baselineDescription);
            return this;
        }

        public FlywayConfigurationOptions sqlMigrationSuffix(final String sqlMigrationSuffix) {
            flywayOptions.put(FlywayOptions.SQL_MIGRATION_SUFFIX, sqlMigrationSuffix);
            return this;
        }

        public FlywayConfigurationOptions baselineOnMigrate(final Boolean baselineOnMigrate) {
            flywayOptions.put(FlywayOptions.BASELINE_ON_MIGRATE, baselineOnMigrate);
            return this;
        }

        public FlywayConfigurationOptions outOfOrder(final Boolean outOfOrder) {
            flywayOptions.put(FlywayOptions.OUT_OF_ORDER, outOfOrder);
            return this;
        }

        public FlywayConfigurationOptions callbacks(final FlywayCallback... callbacks) {
            flywayOptions.put(FlywayOptions.CALLBACKS, callbacks);
            return this;
        }

        public FlywayConfigurationOptions resolvers(final MigrationResolver... resolvers) {
            flywayOptions.put(FlywayOptions.RESOLVERS, resolvers);
            return this;
        }

        public FlywayConfigurationOptions skipDefaultCallback(final Boolean skipDefaultCallback) {
            flywayOptions.put(FlywayOptions.SKIP_DEFAULT_CALLBACK, skipDefaultCallback);
            return this;
        }

        public FlywayConfigurationOptions skipDefaultResolvers(final Boolean skipDefaultResolvers) {
            flywayOptions.put(FlywayOptions.SKIP_DEFAULT_RESOLVERS, skipDefaultResolvers);
            return this;
        }

        public FlywayOptions build() {
            return flywayOptions;
        }
    }
}

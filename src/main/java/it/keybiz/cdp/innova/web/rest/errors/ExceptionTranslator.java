package it.keybiz.cdp.innova.web.rest.errors;

import it.keybiz.cdp.innova.domain.EvangelistProcessStep_;
import it.keybiz.cdp.innova.domain.Servizio_;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.web.util.HeaderUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

    private static final String FIELD_ERRORS_KEY = "fieldErrors";
    private static final String MESSAGE_KEY = "message";
    private static final String PATH_KEY = "path";
    private static final String VIOLATIONS_KEY = "violations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Environment env;

    public ExceptionTranslator(Environment env) {
        this.env = env;
    }

    /**
     * Post-process the Problem payload to add the message key for the front-end if needed.
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return null;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }

        HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
        String requestUri = nativeRequest != null ? nativeRequest.getRequestURI() : StringUtils.EMPTY;
        ProblemBuilder builder = Problem
            .builder()
            .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType())
            .withStatus(problem.getStatus())
            .withTitle(problem.getTitle())
            .with(PATH_KEY, requestUri);

        if (problem instanceof ConstraintViolationProblem) {
            builder
                .with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations())
                .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION);
        } else {
            builder.withCause(((DefaultProblem) problem).getCause()).withDetail(problem.getDetail()).withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey(MESSAGE_KEY) && problem.getStatus() != null) {
                builder.with(MESSAGE_KEY, "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result
            .getFieldErrors()
            .stream()
            .map(f ->
                new FieldErrorVM(
                    f.getObjectName().replaceFirst("DTO$", ""),
                    f.getField(),
                    StringUtils.isNotBlank(f.getDefaultMessage()) ? f.getDefaultMessage() : f.getCode()
                )
            )
            .collect(Collectors.toList());

        Problem problem = Problem
            .builder()
            .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
            .withTitle("Method argument not valid")
            .withStatus(defaultConstraintViolationStatus())
            .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION)
            .with(FIELD_ERRORS_KEY, fieldErrors)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        return create(
            ex,
            request,
            HeaderUtil.createFailureAlert(applicationName, false, ex.getEntityName(), ex.getErrorKey(), ex.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
        Problem problem = Problem.builder().withStatus(Status.CONFLICT).with(MESSAGE_KEY, ErrorConstants.ERR_CONCURRENCY_FAILURE).build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNoValuePresent(NoSuchElementException ex, NativeWebRequest request) {
        Problem problem = Problem.builder().withStatus(Status.NOT_FOUND).with(MESSAGE_KEY, ErrorConstants.ERR_NOT_FOUND).build();
        return create(ex, problem, request);
    }

    // nomi constraint generati da liquibase
    private final Map<String, String> constraintMap = Map.of(
        "FK6d3r9ih4x8o2axdy41lnyixuv", "Non è possibile eliminare un programma che contiene una o più sfide",
        "FKa56ffuif886ok13p0l0hrr1q3", "Non è possibile eliminare un creator appartenente ad uno o più contributi",
        "FKdr17ca4jqne1hvycfcqf338kd", "Non è possibile eliminare un ruolo appartenente ad uno o più evangelist",
        "FKobmn2cm0hu1tiy8xksnj1a6m7", "Non è possibile eliminare una sfida che contiene uno o più contributi",
        "FKprit5j1nnm0f0k8sjtbhwsa2c", "Non è possibile eliminare un unità organizzativa che contiene uno o più ruoli",
        "FKstbexgvhursee14uulmovlwhd", "Non è possibile eliminare una community che contiene uno o più programmi"
    );

    @ExceptionHandler
    public ResponseEntity<Problem> handleDataIntegrityViolation(DataIntegrityViolationException ex, NativeWebRequest request) {
        String message = ex.getCause().getMessage();

        if (ex.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cause = (ConstraintViolationException) ex.getCause();
            message = constraintMap.getOrDefault(cause.getConstraintName(), message);
        }

        Problem problem = Problem.builder()
            .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
            .withTitle("Data Integrity Violation")
            .withStatus(Status.FORBIDDEN)
            .with(FIELD_ERRORS_KEY, message)
            .build();

        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUniqueConflict(JpaSystemException ex, NativeWebRequest request) {
        List<String> errors = new ArrayList<>();

        if (ex.getMostSpecificCause() instanceof PSQLException) {
            PSQLException cause = (PSQLException) ex.getMostSpecificCause();

            // nomi constraint generati da liquibase
            if (cause.getMessage().contains("uc_cdp_serviceposition_col")) {
                errors.add(Servizio_.POSITION);
            } else if (cause.getMessage().contains("uc_cdp_evangelist_process_stepposition_col")) {
                errors.add(EvangelistProcessStep_.POSITION);
            }
        }

        Problem problem = Problem.builder()
            .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
            .withTitle("Unique Constraint Violation")
            .withStatus(Status.CONFLICT)
            .with(FIELD_ERRORS_KEY, errors.isEmpty() ? "Constraint violation non mappata!" : errors)
            .build();

        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleGenericError(Exception ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .with(MESSAGE_KEY, ErrorConstants.INTERNAL_SERVER_ERROR)
            .build();
        return create(ex, problem, request);
    }

    @Override
    public ProblemBuilder prepare(final Throwable throwable, final StatusType status, final URI type) {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());

        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            if (throwable instanceof HttpMessageConversionException) {
                return Problem
                    .builder()
                    .withType(type)
                    .withTitle(status.getReasonPhrase())
                    .withStatus(status)
                    .withDetail("Unable to convert http message")
                    .withCause(
                        Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
                    );
            }
            if (throwable instanceof DataAccessException) {
                return Problem
                    .builder()
                    .withType(type)
                    .withTitle(status.getReasonPhrase())
                    .withStatus(status)
                    .withDetail("Failure during data access")
                    .withCause(
                        Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
                    );
            }
            if (containsPackageName(throwable.getMessage())) {
                return Problem
                    .builder()
                    .withType(type)
                    .withTitle(status.getReasonPhrase())
                    .withStatus(status)
                    .withDetail("Unexpected runtime exception")
                    .withCause(
                        Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
                    );
            }
        }

        return Problem
            .builder()
            .withType(type)
            .withTitle(status.getReasonPhrase())
            .withStatus(status)
            .withDetail(throwable.getMessage())
            .withCause(
                Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
            );
    }

    private boolean containsPackageName(String message) {
        // This list is for sure not complete
        return StringUtils.containsAny(message, "org.", "java.", "net.", "javax.", "com.", "io.", "de.", "it.keybiz.cdp.innova");
    }
}

TRUNCATE TABLE available_slots, users RESTART IDENTITY CASCADE;

INSERT INTO users (created_at, updated_at, version, username, password)
SELECT
    now(),
    now(),
    0,
    'user_' || gs,
    'password'
FROM generate_series(1, 1000000) AS gs;


INSERT INTO available_slots (
    created_at,
    updated_at,
    version,
    start_time,
    end_time,
    is_reserved,
    holder_id
)
SELECT
    now(),
    now(),
    0,
    now() + (gs || ' minutes')::interval,
    now() + ((gs + 30) || ' minutes')::interval,
    false,
    NULL
FROM generate_series(1, 1000000) AS gs;

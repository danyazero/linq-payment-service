CREATE TABLE transaction (
                             id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             order_id text NOT NULL,
                             recipient_id text NOT NULL,
                             payer_id text NOT NULL,
                             amount double precision NOT NULL DEFAULT '0'::double precision
);

CREATE TABLE history (
                         id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         status text NOT NULL,
                         timestamp text NOT NULL DEFAULT now(),
                         transaction_id integer REFERENCES transaction(id)
);
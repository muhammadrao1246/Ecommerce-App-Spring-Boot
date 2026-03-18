INSERT INTO brand (id,name) VALUES (1, 'LogiPro'), (2, 'Voltix'), (3,'PaperMate'), (4,'Stride'), (5,'Hydra'), (6,'HomeWare'), (7,'BrightLite'), (8,'CottonCo'), (9,'TrailPack'), (10,'SoundBeat') ON CONFLICT (id) DO NOTHING;
INSERT INTO category (id,name) VALUES (1,'Electronics'), (2,'Books'), (3,'Sports'), (4,'Home'), (5,'Clothing'), (6,'Accessories') ON CONFLICT (id) DO NOTHING;
INSERT INTO product (id, name, description, brand_id, category_id, price, quantity) VALUES
                                                                                        ('8f4c6b2e-0f8a-4c8a-8c1d-0b1b4e2a1f20','Wireless Mouse', 'Ergonomic 2.4G wireless mouse', 1, 2, 19.99, 45.00),
                                                                                        ('2a1b8c7d-7c6f-4c9b-9a3f-6e1e2d3c4b5a','USB-C Charger 65W', 'Fast charging wall adapter', 2, 1, 29.50, 30.00),
                                                                                        ('c3b2a1d0-4e5f-4a6b-8c9d-0e1f2a3b4c5d','Notebook A5', 'Hardcover ruled notebook (200 pages)', 3, 2, 6.75, 120.00),
                                                                                        ('4d5c6b7a-8e9f-4a1b-9c2d-3e4f5a6b7c8d','Running Shoes', 'Lightweight daily trainer', 5, 5, 79.99, 18.00),
                                                                                        ('5a6b7c8d-9e0f-4b1a-8c2d-3e4f5a6b7c8d','T-Shirt', 'Cotton t-shirt', 6, 5, 29.99, 100.00),
                                                                                        ('6b7c8d9e-0f1a-4c2b-9d3e-4f5a6b7c8d9e','Sweatshirt', 'Cotton sweatshirt', 6, 5, 39.99, 100.00),
                                                                                        ('7c8d9e0f-1a2b-4d3c-8e4f-5a6b7c8d9e0f','Pants', 'Cotton pants', 6, 5, 49.99, 100.00),
                                                                                        ('9e0f1a2b-3c4d-4e5f-8a6b-7c8d9e0f1a2b','Socks', 'Cotton socks', 6, 5, 19.99, 100.00)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO user_roles


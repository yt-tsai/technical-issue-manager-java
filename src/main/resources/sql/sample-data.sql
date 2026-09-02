-- Initial Issue data based on the Python prototype.
INSERT IGNORE INTO issues (
    id, title, customer, product, priority, status, progress,
    assignee, due_date, description, created_at, updated_at
) VALUES
    (1, 'Customer login bug', 'ABC Corp', 'Web Portal', 'High', 'In Progress', 40, 'Peter', '2026-09-05', 'Login fails when using special characters in password.', '2026-08-31 09:00:00', '2026-09-01 14:45:39'),
    (2, 'Data export request', 'XYZ Ltd', 'Admin Dashboard', 'Medium', 'Open', 0, 'Mika', '2026-09-08', 'Need CSV export for monthly report.', '2026-08-31 09:10:00', '2026-08-31 09:10:00'),
    (3, 'UI alignment issue', 'IT School', 'Student Portal', 'Low', 'Resolved', 100, 'Ken', '2026-09-01', 'Button alignment is slightly off on mobile view.', '2026-08-31 09:20:00', '2026-08-31 09:20:00'),
    (4, 'API response delay', 'Sunrise Trading', 'Order System', 'High', 'In Progress', 65, 'Peter', '2026-09-10', 'Order list API takes too long during peak hours.', '2026-08-31 09:30:00', '2026-08-31 09:30:00'),
    (5, 'Incorrect total amount', 'Blue Ocean', 'Billing System', 'High', 'Open', 0, 'Yuki', '2026-09-12', 'Invoice total is calculated incorrectly for tax included items.', '2026-08-31 09:40:00', '2026-08-31 09:40:00');

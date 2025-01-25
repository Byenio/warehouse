import { env } from "process";
import WarehouseCard from "./warehouse-card";

export default async function Warehouses() {
    const data = await fetch(
        `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/warehouse`,
        { cache: "force-cache" }
    );
    const warehouses: Warehouse[] = await data.json();

    return (
        <>
            {warehouses.map((warehouse) => (
                <WarehouseCard
                    key={warehouse.id}
                    id={warehouse.id}
                    name={warehouse.name}
                    image={warehouse.imageUrl}
                />
            ))}
        </>
    );
}

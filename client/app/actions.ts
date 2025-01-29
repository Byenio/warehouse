"use server";

import { env } from "process";

export async function deleteProductAction(productId: string) {
    const response = await fetch(
        `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/product/${productId}`,
        {
            method: "DELETE",
        }
    );

    if (response.status != 204) {
        throw new Error("Failed to delete product");
    }
}
